package com.lvoutcity.admin.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.core.OSS.Sms;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.model.SysRole;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserMember;
import com.lvoutcity.model.UserRole;
import com.lvoutcity.model.UserWallet;
import com.lvoutcity.model.Wallet;

/**
 * @author zhanghd
 *time 2016-5-10
 */

public class MemberController extends BaseController {

	/**
	 * 默认方法
	 */
	public void index() {
		render("member-list.html");
}

	/**
	 * 查询用户信息
	 */
	public void selectMember(){
		
		String sqlPrefix = "select tuser.*,date_format(tuser.create_time,'%Y-%m-%d %H:%m:%s') as create_time ,member.status";
		String sqlSuffix = " from t_user AS tuser left join t_user_member as member on tuser.id =member.user_id where 1=1  ";
		User user = getBean(User.class);
		if(user.getId()!=null  ){
			sqlSuffix += " and tuser.user_no = '"+user.getId()+"'   ";
		}
		if(user.getRealName()!=null &&!user.getRealName().isEmpty()){
			sqlSuffix += " and tuser.real_name = '"+user.getRealName()+"' ";
		}
		if(user.getPhoneNumber()!=null &&!user.getPhoneNumber().isEmpty()){
			sqlSuffix += " and  tuser.phone_number like '%"+user.getPhoneNumber()+"%'";
		}
		if(user.getIdcard()!=null &&!user.getIdcard().isEmpty()){
			sqlSuffix += " and tuser.idcard = '"+user.getIdcard()+"' ";
		}
		renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				sqlPrefix , sqlSuffix+" order by tuser.user_no asc" ));
	}

	/**
	 * 用户删除
	 */
	@SuppressWarnings("rawtypes")
	public void delUser(){
		String id = getRequest().getParameter("id").toString();
		String sqlSuffix = " from  t_user ,t_user_vip_log,t_vip_consume_log where t_user.id="+id +""+" and t_user.id=t_user_vip_log.user_id and t_user_vip_log.id =t_vip_consume_log.id";
		
		List a =Db.find("SELECT t_vip_consume_log.order_id "+""+sqlSuffix);
		if(a.size()>0){
		 renderJson("result", "false");
	 }else {
			Db.deleteById("t_user","id",id);
		    renderJson("result", "ok");
	 }
	}

	/**
	 * 查看用户详细信息
	 */
	public void memberShow(){
		User user = User.dao.findById(getPara("id"));
		UserMember member = UserMember.dao.find("select * from  t_user_member where user_id = '"+user.getId()+"'").get(0);
		User user1 = User.dao.findById(member.getRefereeLevel1());
		setAttr("user", user);
		member.setReferral(LvoutcityUtils.isNotNull(user1) ? user1.getPhoneNumber() : null);
		setAttr("member", member);
		UserWallet use =UserWallet.dao.findByUserId(user.getId());
		setAttr("gold", use.getMoney());
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",2);
		render("memberShow.html");
	}


	/**
	 * 俱乐部信息编辑
	 */
	public void reset(){
		/*User user = User.dao.findById((getPara(0)));
		UserExt ext = UserExt.dao.findById((getPara(0)));
		UserRole userRole = UserRole.dao.find("select * from t_user_role where user_id="+getPara()).get(0);
		SysRole sysRole = SysRole.dao.findById(userRole.getId());
		setAttr("user", user);
		setAttr("ext",ext);
		setAttr("sysRole",sysRole);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",3);*/
	    render("userShow.html");
	}
	
	/**
	 * 
	 * @Title: referral 
	 * @Description: 用户合伙人查询
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016 2016年7月5日 下午9:54:05
	 * @create_user James w.s
	 * @throws
	 */
	public void referral(){
		setAttr("level", getPara("level"));
		setAttr("userId",getPara("userId"));
		render("referral.html");
	}
	
	/**
	 * 
	 * @Title: referralList 
	 * @Description: 推荐人数据 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016 2016年7月5日 下午10:02:17
	 * @create_user James w.s
	 * @throws
	 */
	public void referralList(){
		try {
			renderDatatableFromDb(UserMember.dao.queryUserLevel(getPara("level"),getBean(User.class),(getDatatableStart() / getDatatableLength()) + 1, getDatatableLength() ));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 用户新增页面
	 */
	public void userAdd(){
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("cl", new TourismClub());
		setAttr("type",1);
		render("user-add.html");
	}

	/**
	 * 启用禁用更新
	 */
	@LogDescription("启用禁用更新")
	public void upateEnable(){
		//new TourismClub().set("", "").update();
		 UserMember.dao.findFirst("select * from t_user_member where user_id= '"+getPara("id")+"'").set("status",getPara("status")).update();
		//getBean(TourismClub.class).save();
		renderJson("success", "false");
	}
	
	
	/**
	 * 
	 * @Title: getUserShopping 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午3:04:46
	 * @create_user James w.s
	 * @throws
	 */
	public void getUserShoppingLog(){
		//String userId = getPara("id");
		SQuery sql = SQuery.create("select * from t_vip_consume_log t ");
		sql.append(" left join t_order_record o ");
		sql.append(" on  o.id = t.order_id ");
		sql.append(" left join ");
		
	}
	
	/**
	 * 查看用户交易记录
	 */
	public void wallet(){
		if(getRequest().getMethod().equalsIgnoreCase("get")){
			String userId = getPara("userId");
			User u =User.dao.findById(userId);
			UserWallet uw = UserWallet.dao.findByUserId(getPara("userId"));
			setAttr("user",u);
			setAttr("user_balance",uw.getMoney());
		}else{
			renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1,
					getDatatableLength(),"select w.status,w.money,w.subject,w.remark,w.balance, DATE_FORMAT(w.create_time,'%Y-%m-%d %T') as create_time_short, ord.order_no ",
					" from t_wallet w left join t_order_record ord on ord.id = w.order_id "
					+ " where w.is_delete!='0' and user_id='"+getPara("userId")+"' order by w.create_time desc"));
		}
	}
	
	
	public void doWithdraw(){
		String money = getPara("money");
		String userId = getPara("userId");
		UserWallet uw = UserWallet.dao.findByUserId(getPara("userId"));
		uw.deduct(new BigDecimal(money));
		uw.update();
		Wallet w = new Wallet();
		w.setId(LvoutcityUtils.getUUID());
		w.setUserId(userId);
		w.setMoney(new BigDecimal(money));
		w.setBalance(uw.getMoney());
		w.setCreateUser(getCurrentUserId());
		w.setSubject("提现");
		w.setStatus(Constants.WALLET.STATUS_OUT);
		w.save();
		renderJson("result","success");
	}
	
	/**
	 * 重置密码
	 * @throws Exception 
	 */
	@LogDescription("重置密码")
	public void resetPassword() throws Exception{
		String userId  = getPara("userId");
		String phone  = getPara("phone");
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 8; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }
	    UserMember u = UserMember.dao.findByUserId(userId);
	    u.setPassword(LvoutcityUtils.encryptPassword(sb.toString()));
	    u.update();
	    if(Sms.sendRestPwdMsg(phone, sb.toString())){
	    	renderJson("result","success");
	    }
	}
}
