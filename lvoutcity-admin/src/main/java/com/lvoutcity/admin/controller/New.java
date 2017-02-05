package com.lvoutcity.admin.controller;

import java.math.BigDecimal;
import java.util.Random;

import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.core.OSS.Sms;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserMember;
import com.lvoutcity.model.UserWallet;
import com.lvoutcity.model.Wallet;

public class New extends BaseController{
	
	@LogDescription("跳转初始页面")
	public void index(){
		render("member-list.html");
	}
	
	@LogDescription("显示会员列表")
	public void getMemberList(){		
//		新建查询sql语句前缀和查询条件
		String sqPrefix = "select tuser.*,date_format(tuser.create_time,'%Y-%m-%d %H:%m:%s') as create_time ,member.status";
		String sqSuffix = " from t_user AS tuser left join t_user_member as member on tuser.id =member.user_id where 1=1  ";
		
		User user = getBean(User.class);
		
		if (user.getId() != null) {
			sqSuffix += "and tuser.user_no = '" + user.getId() + "'";
		}
		if (user.getRealName() != null && !user.getRealName().isEmpty()) {
			sqSuffix += "and tuser.real_name = '" + user.getRealName() + "'";
		}
		if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
			sqSuffix += "and user.phone_number = '" + user.getPhoneNumber() + "'";
		}
		if (user.getIdcard() != null && !user.getIdcard().isEmpty()) {
			sqSuffix += "and user.idcard = '" + user.getIdcard() + "'";
		}
		renderDatatableFromDb(Db.paginate(getDatatableStart()/getDatatableLength()+1,	getDatatableLength(), sqPrefix, sqSuffix+"order by tuser.user_no desc"));
	}
	
	@LogDescription("显示详细会员信息")
	public void memberDetail(){
		User user = User.dao.findById(getPara("id"));
		setAttr("user", user);
		UserMember member = UserMember.dao.find("select * from tuser_user_member where user_id = " + getPara("id")).get(0);
		User user1 = User.dao.findById(member.getRefereeLevel1()); 
		setAttr("user", user1);
		member.setRefereeLevel1(LvoutcityUtils.isNotNull(user1)?user1.getPhoneNumber():null);
		setAttr("member", member);
		UserWallet userWallet  = UserWallet.dao.findById(getPara("id"));
		member.setGoldCoin(userWallet.getMoney().intValue());
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type", 2);
		render("memberShow.htm");
	}
	
	@LogDescription("启用禁用")
	public void memberBlock(){
		UserMember.dao.findFirst("select * from tuser_member where user_id = '" + getPara("id") + "'").set("status",getPara("status")).update();
		renderJson("success","false");
	}
	
	@LogDescription("重置密码")
	public void resetPw() throws Exception{
		String userId  = getPara("userId");
		String phone  = getPara("phone");
		//重置密码
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 8; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }
	    UserMember userMember = UserMember.dao.findByUserId(userId);
	    userMember.setPassword(LvoutcityUtils.encryptPassword(sb.toString()));
	    userMember.update();
//	    给会员发送短信
	    if (Sms.sendRestPwdMsg(phone, sb.toString())) {
			renderJson("result","success");
		}
	}
	
	@LogDescription("查看会员消费记录")
	public void walletShow(){
		if (getRequest().getMethod().equalsIgnoreCase("get")) {
			String userId = getPara("userId");
			User user = User.dao.findById(userId);
			UserWallet wallet = UserWallet.dao.findByUserId(userId);
			setAttr("user", user);
			setAttr("user_balance", wallet.getMoney());
		}else{
			renderDatatableFromDb(Db.paginate(getDatatableStart()/getDatatableLength()+1, getDatatableLength(), "select status,money,subject,remark,balance, DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time ", " from t_wallet where is_delete='1' and user_id='"+getPara("userId")+"' order by create_time desc"));
		}
	}
	
	@LogDescription("会员提现")
	public void doWithdraw(){
		String userId = getPara("userId");
		UserWallet uw = UserWallet.dao.findByUserId("userId");
		String money = getPara("money");
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
	
}
	