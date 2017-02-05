/**
 * 
 */
package com.lvoutcity.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.admin.controller.bean.UserBean;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserMember;

/**
 * @author wj
 *
 */
public class LeaderController extends BaseController {
	
	/**
	 *  主页跳转
	 */
	public void index()throws Exception{
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10010",getRequest()));
		render("indexLeader.html");
	}
	
	/**
	 * 新增领队页面跳转
	 */
	public void leaderAdd()throws Exception{
		UserBean user = new UserBean();
		setAttr("leader", user);
		setAttr("type", 1);
		render("leader-add.html");
	}
	
	/**
	 * 新增领队数据保存
	 */
	@LogDescription("新增领队")
	public void saveLeader()throws Exception{
		//UserBean leader = getBean(UserBean.class);
		User user =getBean(User.class); 
		UserMember userMember = getBean(UserMember.class); 
		// 插入主表
		UUID ud = UUID.randomUUID();
		user.setId(ud.toString().replace("-", ""));
		user.setCreateTime(new Date());
		user.setClubId(getSessionAttr("clubId").toString());
		//user.setClubId(clubId);
		//user.setUserNo(0);
		user.save();
		
		
		// 插入关系表
		userMember.setUserId(ud.toString().replace("-", ""));
		userMember.setId(LvoutcityUtils.getUUID());
		userMember.setCreateUser(getSessionAttr("current_user_id"));
		userMember.save();
		
		renderJson("success", "false");
		
	}
	
	/**
	 * 查询领队列表
	 */
	public void selectLeader()throws Exception{
		String sql = " FROM t_user tu LEFT JOIN t_user_member tue ON tu.id = "
				+ "tue.user_id  LEFT JOIN t_tourism_club  ttc ON tu.club_id = ttc.id where 1 =1 and "
				+ "tue.user_type = '1' ";
		UserBean ub = getBean(UserBean.class);
		if(ub.getUserNo()!=null){
			sql += "and tu.user_no = "+ub.getUserNo() +" ";
		}
		if(ub.getRealName()!=null){
			sql += "and tu.real_name like '%"+ub.getRealName() +"%' ";
		}
		if(ub.getPhoneNumber()!=null){
			sql += "and tu.phone_number = '"+ub.getPhoneNumber() +"' ";
		}
		if(ub.getIdcard()!=null){
			sql += "and tu.idcard like '%"+ub.getIdcard() +"%' ";
		}
		if(ub.getClubId()!=null){
			sql += "and tu.club_id = '"+ub.getClubId() +"' ";
		}

		renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				CommonSql.LEADER_SELECT, sql+" order by tu.user_no asc" ));
		//renderDatatable(page);
	}
	
	/**
	 * 查询领队列表（不分页）
	 */
	public void selectLeaderNoPaging(){
		String sql = " FROM t_user tu LEFT JOIN t_user_member tue ON tu.id = "
				+ "tue.user_id  LEFT JOIN t_tourism_club  ttc ON tu.club_id = ttc.id where 1 =1 and "
				+ "tue.user_type = '1' and tue.leader_status= '1' and tu.club_id = '" + getPara("clubId")+"'";

		List<Record> list = Db.find(CommonSql.LEADER_SELECT+sql+" order by tu.user_no desc");
		renderDatatableFromDb(list);
		//renderDatatable(page);
	}
	
	
	/**
	 * 查看领队信息
	 */
	public void leaderShow()throws Exception{
		//UserBean user = new UserBean();

		String sql = "  FROM t_user tu LEFT JOIN t_user_member tue ON tu.id = "
				+ "tue.user_id  LEFT JOIN t_tourism_club  ttc ON tu.club_id = ttc.id where 1 =1 "
				+ " and tu.id ='"+getPara(0)+"' ";
		//TourismClub tc = TourismClub.dao.findById(getPara(0));
		Record rd = Db.findFirst(CommonSql.LEADER_SELECT+sql);
		Object ubc =  JSON.parse(rd.toJson());
		
		setAttr("leader", ubc);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",2);
		render("leader-add.html");
	}
	
	/**
	 * 启用禁用更新
	 */
	@LogDescription("更改领队状态")
	public void upateEnable()throws Exception{
		//new TourismClub().set("", "").update();
		//new UserMember().set("status", getPara("status")).set("user_id", getPara("id")).update();
		Db.update("update t_user_member set leader_status='"+getPara("status")+"' where user_id='"+getPara("id")+"' ");
		//getBean(TourismClub.class).save();
		renderJson("success", "false");
	}
	
	/**
	 * 根据手机号查询领队成员信息
	 */
	public void selectPhone()throws Exception{
		try {
			String number = getPara("phoneNumber");
			 Pattern pattern = Pattern.compile("[0-9]*"); 
			 Matcher isNum = pattern.matcher(number);
			   if( !isNum.matches() ){
				   renderJson("success","notNum");
			       return ; 
			   } 
			if(number.length() ==11){
				User uct = User.dao.findFirst("select t1.* from t_user t1 left join t_user_member t2 on t1.id = t2.user_id "
						+ " where "
						//+ "t2.status = '1' and "
						+ " t1.phone_number = '"+getPara("phoneNumber")+"' ");
				if(uct == null){
					renderJson("success","null");
				}else{
					UserMember uu = UserMember.dao.findByUserId(uct.getId());
					if("1".equals(uu.getUserType())){
						renderJson("success","have");
					}else if("0".equals(uu.getStatus())){
						renderJson("success","forbiden");
					}else{
						renderJson("success",uct);
					}
				}
			}else{
				renderJson("success","false");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// 将会员绑定为领队
	@LogDescription("新增领队")
	public void updateLeader()throws Exception{
		//User.dao.findById(getPara("id")).set("category", "1").update();
		Db.update("update t_user_member set user_type = '1',update_time=now() where user_id= '"+getPara("id")+"' ");
		Db.update("update t_user set club_id = '"+getSessionAttr("clubId").toString()+"',create_user ='"+getSessionAttr("current_user_id")+"'  where id= '"+getPara("id")+"' ");
		renderJson("success","false");
	}
	
	@LogDescription("删除领队")
	public void deleteLeader()throws Exception{
		//User.dao.findById(getPara("id")).set("category", "1").update();
		String idList = getPara("idList");
		JSONArray data = JSONArray.parseArray(idList);
		String listData ="'-1'";
		for(int i =0;i<data.size();i++){
			listData += ",'"+data.get(i)+"'";
		}
		Db.update("update t_user_member set user_type = '0' where user_id in ("+listData+") ");
		Db.update("update t_user set club_id = '' where id in ("+listData+") ");
		renderJson("success","false");
	}
	
	
	
}
