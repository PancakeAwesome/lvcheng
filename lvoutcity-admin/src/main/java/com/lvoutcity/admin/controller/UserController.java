package com.lvoutcity.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.admin.controller.bean.UserBean;
import com.lvoutcity.core.OSS.Sms;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.SysRole;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;
import com.lvoutcity.model.UserRole;

/**
 * @author zhanghd
 *time 2016-5-9
 */

public class UserController extends BaseController {

	
	private static final Log log = LogFactory.getLog(UserController.class);
	
	/**
	 * 默认方法
	 */
	public void index() {
		render("indexUser.html");
	}

//	postmaster@lvoutcity.com,
//	密码：LVoutdoor456
	
	/**
	 * 查询用户信息
	 */
	public void selectUser(){
		try {
			StringBuilder stringBuilderPrefix = new StringBuilder(
					"select  tuser.id,tuser.user_no,tuser.nick_name,tuser.gender,");
			stringBuilderPrefix.append("tuser.phone_number,bc.user_name as create_user,b.status,c.club_name, ");
			stringBuilderPrefix.append(" date_format(b.create_time,'%Y-%m-%d %T') as create_time ");
			StringBuilder stringBuilderSuffix = new StringBuilder(" from t_user_backgroup as b ");
			stringBuilderSuffix.append(" left join t_user tuser on tuser.id = b.user_id ");
			stringBuilderSuffix.append(" left join t_tourism_club c on b.club_id = c.id ");
			stringBuilderSuffix.append(" left join t_user_backgroup bc on bc.user_id = b.create_user ");
			stringBuilderSuffix.append(" where 1=1 and b.is_delete = 1 ");
			//stringBuilderSuffix.append(" and tuser.id !='"+Constants.ADMIN_ID+"'");

			UserBean user = getBean(UserBean.class);

			if (user.getUserNo() != null) {
				stringBuilderSuffix.append(" and tuser.user_no = '" + user.getUserNo() + "'   ");
			}
			if (user.getNickName() != null && !user.getNickName().isEmpty()) {
				stringBuilderSuffix.append(" and tuser.nick_name = '" + user.getNickName() + "' ");
			}
			if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
				stringBuilderSuffix.append(" and  tuser.phone_number like '%"+user.getPhoneNumber()+"%'");
			}
			renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
					stringBuilderPrefix.toString(), stringBuilderSuffix.toString() + " order by b.create_time desc"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用户删除
	 */
	public void delUser(){
		String id = "";
		List<HashMap<String, String>> idList = getListMap("id");
		for (int i = 0; i < idList.size(); i++) {
			id = idList.get(i).get("id");
			Db.update(" update t_user_backgroup set is_delete = '0' where user_id='"+id+"'");
		}
//		String sqlSuffix = " from  t_user ,t_user_vip_log,t_vip_consume_log where t_user.id='"+id +"' "+" and t_user.id=t_user_vip_log.user_id and t_user_vip_log.id =t_vip_consume_log.id";
//		List a =Db.find("SELECT t_vip_consume_log.order_id "+""+sqlSuffix);
//		if(a.size()>0){
//		 renderJson("result", "false");
//	 }else {
		 
		 renderJson("result", "ok");
	// }
	}

	/**
	 * 查看用户详细信息
	 */
	public void userShow(){
		User user = User.dao.find("select * from t_user where user_no ="+getPara(0)).get(0);
        UserBackgroup backgroup = UserBackgroup.dao.findFirst("select * from t_user_backgroup where user_id='"+user.getId()+"'");
        List<TourismClub> list = TourismClub.dao.find("select * from t_tourism_club where enabled ='1' and is_delete='0' ");
		List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
		for(TourismClub t : list){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", t.getId());
			m.put("name", t.getClubName());
			map.add(m);
		}
		net.sf.json.JSONArray ss = net.sf.json.JSONArray.fromObject(map);
		setAttr("list",ss);
		List<UserRole> userRole = UserRole.dao.find("select * from t_user_role where user_id="+getPara(0));
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sbId = new StringBuffer();
		for(UserRole  role :userRole){
			SysRole sysRole = SysRole.dao.findById(role.getRoleId());
			sb.append(sysRole.getRoleName()).append(",");
			sbId.append(sysRole.getId()).append(",");
		}
		user.setRoleName(sb.substring(0,sb.lastIndexOf(",")));
		user.setRoleIds(sbId.substring(0,sbId.lastIndexOf(",")));
		
		setAttr("user", user);
		setAttr("backgroup",backgroup);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",2);
		render("userShow.html");
	}

	/**
	 * 俱乐部信息编辑
	 */
	public void reset(){
	/*	User user = User.dao.findById((getPara(0)));
		UserExt ext = UserExt.dao.findById((getPara(0)));
		UserClub userClub = UserClub.dao.findById(getPara(0));
		TourismClub tourismClub = TourismClub.dao.findById(userClub.getClubId());
		UserRole userRole = UserRole.dao.find("select * from t_user_role where user_id="+getPara()).get(0);
		SysRole sysRole = SysRole.dao.findById(userRole.getId());
		setAttr("user", user);
		setAttr("ext",ext);
		setAttr("tourismClub",tourismClub);
		setAttr("sysRole",sysRole);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",3);*/
	    render("userShow.html");
	}

	
	/**
	 * 
	 * @Title: saveOrUpdate 
	 * @Description: 修改或添加
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午5:21:17
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdate() {
		try {
			User bean = getBean(User.class);
		    //判断是否存在手机号码（唯一确定）
			User user = User.dao.findByPhoneNumber(bean.getPhoneNumber());
			// 用户检查
			if (LvoutcityUtils.isNull(user)) {
				renderJson("error", "用户名不存在，请先注册!");
				return;
			}
			
			UserBackgroup bc = User.dao.queryUserId(user.getId());
			if (bc == null) {
				
				bc = new UserBackgroup();
				bc.setUserId(user.getId());
				bc.setId(LvoutcityUtils.getUUID());
				bc.setClubId(bean.getClubId());
				user.setClubId(bean.getClubId());
				bc.setUserName(bean.getPhoneNumber());
				bc.setStatus(bean.getStatus());
				bc.setCreateTime(new Date());
				bc.setCreateUser(getSessionAttr("current_user_id"));
				bc.setIsDelete(Constants.ISDELETE_FALSE);
				String pwd = LvoutcityUtils.getRandomString(1, 8);
				bc.setPassword(LvoutcityUtils.encryptPassword(pwd));
				Sms.sendUserComplete(user.getPhoneNumber(), pwd);
				bc.save();
				user.update();
				if (LvoutcityUtils.isNotNull(bean.getRoleIds())) {
					List<String> role = LvoutcityUtils.parseToListSplit(bean.getRoleIds(), ",");
					for (String str : role) {
						UserRole userRole = new UserRole();
						userRole.setId(LvoutcityUtils.getUUID());
						userRole.setUserId(user.getId());
						userRole.setRoleId(str);
						userRole.save();
					}
				}
				renderJson("success", "添加成功!");
			} else {
				if (LvoutcityUtils.isNotNull(user)) {
					if (LvoutcityUtils.isNotNull(bean.getClubId())) {
						bc.set("club_id", bean.getClubId());
						user.setClubId(bean.getClubId());
					}
					if (LvoutcityUtils.isNotNull(bean.getStatus())) {
						bc.set("status", bean.getStatus());
					}
					bc.update();
					user.update();
					if (LvoutcityUtils.isNotNull(bean.getRoleIds())) {
						List<UserRole> list = UserRole.dao
								.find("select * from t_user_role u where u.user_id ='" + user.getId() + "'");
						if (!list.isEmpty()) {
							Db.update("delete from t_user_role  where user_id ='" + user.getId() + "'");
						}
						List<String> role = LvoutcityUtils.parseToListSplit(bean.getRoleIds(), ",");
						List<UserRole> userRoleList = new ArrayList<UserRole>();
						for (String str : role) {
							UserRole userRole = new UserRole();
							userRole.setId(LvoutcityUtils.getUUID());
							userRole.setUserId(user.getId());
							userRole.setRoleId(str);
							userRoleList.add(userRole);
						}
						Db.batchSave(userRoleList, userRoleList.size());
					}
				}
				renderJson("success", "修改成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e);
			renderJson("error", "操作异常!请联系管理员!");
		}

	}
	
	
	/**
	 * 
	 * @Title: getPhoneValid 
	 * @Description: 根据用户手机进行查询
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午6:55:04
	 * @create_user James w.s
	 * @throws
	 */
	public void getPhoneValid(){
		String phone = getPara("phone");
		
		//  根据用户手机号查询信息
		if(LvoutcityUtils.isNotNull(phone)){
			User user = User.dao.findByPhoneNumber(phone);
			if(LvoutcityUtils.isNotNull(user)){
				UserBackgroup bakc = User.dao.queryUserId(user.getId());
				if(LvoutcityUtils.isNull(bakc)){
					renderJson("success", user);
					return;
				}
				renderJson("error", "会员已经注册过了!");
				return;
			}
		}
		renderJson("error", "会员不存在!");
	}
	
	
	
	/**
	 * 用户新增页面
	 */
	public void userAdd(){
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		try {
			setAttr("user", new User());
			setAttr("back", new UserBackgroup());
			setAttr("type",1);
			setAttr("LvId",Constants.LVC_CLUB_ID);
			render("user-add.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title: userEdit 
	 * @Description: 用户修改
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午9:04:28
	 * @create_user James w.s
	 * @throws
	 */
	public void userEdit(){
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		try {
			User user = User.dao.findById(getPara("id"));
	        UserBackgroup backgroup = UserBackgroup.dao.findFirst("select * from t_user_backgroup where user_id='"+user.getId()+"'");
			List<UserRole> userRole = UserRole.dao.find("select * from t_user_role where user_id= '"+user.getId()+"'");
			StringBuffer sb = new StringBuffer();
			StringBuffer sbId = new StringBuffer();
			for(UserRole  role :userRole){
				SysRole sysRole = SysRole.dao.findById(role.getRoleId());
				sb.append(sysRole.getRoleName()).append(",");
				sbId.append(sysRole.getId()).append(",");
			}
			user.setUserName(backgroup.getUserName());
			if(LvoutcityUtils.isNotNull(sb) && LvoutcityUtils.isNotNull(sbId)){
				user.setRoleName(sb.substring(0,sb.lastIndexOf(",")));
				user.setRoleIds(sbId.substring(0,sbId.lastIndexOf(",")));
			}
			user.setClubId(backgroup.getClubId());
			
			setAttr("user", user);
			setAttr("LvId",Constants.LVC_CLUB_ID);
			setAttr("backgroup",backgroup);
			// 设置页面类型 1 是新增 2是查看  3 是编辑
			setAttr("type",3);
			render("user-add.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 启用禁用更新
	 */
	public void upateEnable(){
		//new TourismClub().set("", "").update();
		System.out.println(getPara("isDelete"));
		User user = User.dao.findById(getPara("id"));
		if(LvoutcityUtils.isNotNull(user)){
			UserBackgroup bakc = User.dao.queryUserId(user.getId());
			if(LvoutcityUtils.isNotNull(bakc)){
				bakc.set("status", getPara("isDelete"));
				bakc.update();
				renderJson("success", "修改成功!");
				return;
			}
		}
		renderJson("error", "数据不存在!");
	}
	
	
	/**
	 * 重置密码
	 * @throws Exception 
	 */
	@LogDescription("重置密码")
	public void resetPassword() throws Exception{
		String userId  = getPara("userId");
		String phone  = getPara("phone");
		String pwd = LvoutcityUtils.getRandomString(1, 8);
	    UserBackgroup u =UserBackgroup.dao.findByUserId(userId);
	    u.setPassword(LvoutcityUtils.encryptPassword(pwd));
	    u.update();
	    if(Sms.sendRestPwdMsg(phone, pwd)){
	    	renderJson("result","success");
	    }
	    
	}
}
