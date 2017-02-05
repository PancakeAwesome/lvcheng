package com.lvoutcity.admin.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.model.Account;

/**
 * @author leix
 *time 2016-5-9
 */

public class AccountController extends BaseController {

	/**
	 * 默认方法
	 */
	public void index() {
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10017",getRequest()));
		render("account.html");
	}
	
	public void unAccountedOrders() {
		render("orders.html");
	}

	@Before(POST.class)
	public void selectAccount(){
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("clubId") != null && !getPara("clubId").isEmpty())
			params.put("c.id", getPara("clubId"));
		if (getPara("account_status") != null && !getPara("account_status").isEmpty())
			params.put("a.account_status", getPara("account_status"));
		Page<Record> data = Account.dao.conditionedPaginate(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir());
		renderDatatableFromDb(data);
	}
	
	public void detail() {
		if (getPara("account_id") != null && !getPara("account_id").isEmpty())
			setAttr("account_id",getPara("account_id"));
//		createAccount();
		render("detail.html");
	}
	
	public void getDetail(){
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("account_id") != null && !getPara("account_id").isEmpty())
			params.put("a.account_id", getPara("account_id"));
//		if (getPara("account_status") != null && !getPara("account_status").isEmpty())
//			params.put("a.account_status", getPara("account_status"));
		Page<Record> data = Account.dao.detailPaginate(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir());
		renderDatatableFromDb(data);
	}
	

	

	
	public void update() {
		Account account = Account.dao.findById(getPara("id"));
		String success = "操作成功！";
		if (getPara("account_status") != null && !getPara("account_status").isEmpty()){
			
			if(getPara("account_status").equals(Constants.ACCOUNT.STATUS_CONFIRMED)){
				if(!account.getAccountStatus().equals(Constants.ACCOUNT.STATUS_NOT_CONFIRMED)){
					renderJson("error", "只有待确认状态的订单可以进行对账确认!");
					return;
				}
				success = "账单确认成功！";
			}else if(getPara("account_status").equals(Constants.ACCOUNT.STATUS_CLEAR)){
				if(!account.getAccountStatus().equals(Constants.ACCOUNT.STATUS_CONFIRMED)){
					renderJson("error", "只有已确认状态的订单可以进行结算!");
					return;
				}
				success = "账单结算成功！";
			}
			account.setAccountStatus(getPara("account_status"));
		}
		if (getPara("note") != null && !getPara("note").isEmpty())
			account.setNote(getPara("note"));
		
		if (account.update())
			renderJson("success", success);
		else
			renderJson("error", "账单状态更新失败!");
	}
}
