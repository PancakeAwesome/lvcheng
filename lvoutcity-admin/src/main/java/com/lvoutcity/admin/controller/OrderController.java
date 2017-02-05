package com.lvoutcity.admin.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.pay.alipay.AlipayRefundRequest;
import com.lvoutcity.core.pay.weixin.UnifiedRefundRequest;
import com.lvoutcity.core.pay.weixin.UnifiedRefundResponse;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.OrderRecord;
import com.lvoutcity.model.PayLog;
import com.lvoutcity.model.TouristInfo;
import com.lvoutcity.model.UserWallet;
import com.lvoutcity.model.Wallet;

public class OrderController extends BaseController {


	
	public void index() {
		setAttr("route_no",getPara("route_no"));
		setAttr("mission_time",getPara("mission_time"));
	}

	@Before(POST.class)
	public void orders() {
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("orderNo") != null && !getPara("orderNo").isEmpty())
			params.put("order_no", getPara("orderNo"));
		if (getPara("routeNo") != null && !getPara("routeNo").isEmpty())
			params.put("rm.route_no", getPara("routeNo"));
		if (getPara("clubId") != null && !getPara("clubId").isEmpty())
			params.put("rm.club_id", getPara("clubId"));
		if (getPara("corpsTime") != null && !getPara("corpsTime").isEmpty())
			params.put("c.mission_time", getPara("corpsTime"));
		if (getPara("status") != null && !getPara("status").isEmpty())
			params.put("ord.status", getPara("status"));
		if (getPara("accountStatus") != null && !getPara("accountStatus").isEmpty())
			params.put("ord.account_status", getPara("accountStatus"));
		
		renderDatatableFromDb(OrderRecord.dao.conditionedPaginate(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir()));
	}

	@Before(POST.class)
	public void ordersForFront() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		if (LvoutcityUtils.isNotNull(getSessionAttr("clubId")) && !Constants.LVC_CLUB_ID.equals(getSessionAttr("clubId")))
			params.put("rm.club_id", getSessionAttr("clubId"));
		
		
		renderDatatableFromDb(OrderRecord.dao.conditionedPaginate(params, 1,
				5, getDatatableOrderCol(), getDatatableOrderDir()));
	}
	
	public void edit(){
		try {
			String id = getPara("id");
			String type = getPara("type");
			Record order = OrderRecord.dao.getDetailById(id);
			List<TouristInfo> tourists = TouristInfo.dao.find("select * from t_tourist_info where order_id = '"+id+"'");
			setAttr("id",id);
			setAttr("type",type);
			setAttr("order",order);
			setAttr("tourists",tourists);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}

	public void ordersByCorps(){
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("routeNo") != null && !getPara("routeNo").isEmpty())
			params.put("rm.route_no", getPara("routeNo"));
		if (getPara("clubId") != null && !getPara("clubId").isEmpty())
			params.put("rm.club_id", getPara("clubId"));
		if (getPara("corpsTime") != null && !getPara("corpsTime").isEmpty())
			params.put("c.mission_time", getPara("corpsTime"));

		
		renderDatatableFromDb(OrderRecord.dao.groupByCorpsPaginate(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir()));
	}
	
	/**
	 * 拒绝退款申请
	 */
	@LogDescription("拒绝退款申请")
	public void refuseRefund(){
		String id = getPara("id");
		OrderRecord o = OrderRecord.dao.findById(id);
		o.setStatus(Constants.ORDER.STATUS_PAID);
		o.update();
		renderJson("result","success");
	}
	
	/**
	 * 通过退款申请
	 */
	@LogDescription("订单退款")
	public void refund() {
		try {
			String id = getPara("id");
			String type = getPara("type");
			OrderRecord o = OrderRecord.dao.findById(id);
			cancelOrder(o); // 现做业务操作，操作完了改订单状态
			
			if (type.equals("2")) {// revoke back
				o.setStatus(Constants.ORDER.STATUS_CANCLED_BACK);
			} else if (type.equals("3")) {// revoke user
				o.setStatus(Constants.ORDER.STATUS_CANCLED_USER);
			}
			o.update();
			renderJson("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}
	}
	
	/**
	 * 退款的具体业务逻辑
	 * @param ord
	 * @throws Exception
	 */
	private void cancelOrder(OrderRecord ord) throws Exception{
		if(checkOrderStatus(ord,Constants.ORDER.STATUS_UNPAID)){ 
			//未支付状态（按照设计，未支付状态不可以后台撤销）
			throw new Exception("待支付状态订单无法由后台取消！");
			/*
			//1.order->已取消;
			ord.setStatus(Constants.ORDER.STATUS_CANCLED_USER);
			ord.update();
			//2.金币预分润和预支出的记录删除;
			Wallet.dao.cancelPredone(ord.getId());
			//3.游客信息删掉;
			TouristInfo.dao.cancelTourist(ord.getId());
			//4.退金币
			Wallet w = Wallet.dao.findFirst("select * from t_wallet where status='"+Constants.WALLET.STATUS_OUT+"' and order_id='"+ord.getId()+"' order by create_time desc");
			if(w!=null){
				w.setId(LvoutcityUtils.getUUID());
				w.setStatus(Constants.WALLET.STATUS_IN);
				w.setCreateTime(null);
				UserWallet uw = UserWallet.dao.findByUserId(ord.getCreateUser());
				w.setBalance(uw.add(w.getMoney()));
				w.setSubject("退款");
				w.save();
				uw.update();
			}*/
		}else if(checkOrderStatus(ord,Constants.ORDER.STATUS_PAID)||checkOrderStatus(ord,Constants.ORDER.STATUS_REFUND_APPLY)){//已支付状态
			//1.退款
			PayLog log = ord.latestSuccessLog();
			if(log==null||!log.getOperation().equals(Constants.PAY.OPERATION_PAY)){
				throw new Exception("支付记录有误，无法退款");
			}
			
			String tpp = log.getPayType();
			
			
			//2.退分润    
			// 分润正式入账是在行程结束后。退款时不需考虑余额。
			Wallet.dao.cancelPredone(ord.getId());
			
			//3.游客信息删掉;
			TouristInfo.dao.cancelTourist(ord.getId());
			
			//4.退金币
			Wallet w = Wallet.dao.findFirst("select * from t_wallet where status='"+Constants.WALLET.STATUS_OUT+"' and order_id='"+ord.getId()+"' order by create_time desc");
			if(w!=null){
				w.setId(LvoutcityUtils.getUUID());
				w.setStatus(Constants.WALLET.STATUS_IN);
				w.setCreateTime(null);
				UserWallet uw = UserWallet.dao.findByUserId(ord.getCreateUser());
				w.setBalance(uw.add(w.getMoney()));
				w.setSubject("退款");
				w.save();
				uw.update();
			}
			
			if(tpp.equals(Constants.PAY.TYPE_ALIPAY)){
				alipayRefund(ord,log.getTppTradeNo());
				// 在异步通知里写log
			}else if (tpp.equals(Constants.PAY.TYPE_WXPAY)){
				wxRefund(ord);
				// 写log
				PayLog refundLog = PayLog.dao.buildLog(ord, Constants.PAY.TYPE_WXPAY, Constants.PAY.STATUS_SUCCESS);
				refundLog.setOperation(Constants.PAY.OPERATION_REFUND);
				refundLog.setCreateUser(getCurrentUserId());
				refundLog.update();
				
			}
		}else{
			throw new Exception("无法取消订单！");
		}
	}
	
	/**
	 * 微信退款请求
	 * @param ord
	 * @throws Exception
	 */
	private void wxRefund (OrderRecord ord) throws Exception{
		UnifiedRefundRequest request = new UnifiedRefundRequest();
		request.initialize();
		request.generateNonceStr();
		request.setOut_trade_no(ord.getOrderNo().toString());
		request.setOut_refund_no(ord.getOrderNo().toString());
		Integer fee = Integer.valueOf(ord.getFeeCash().multiply(new BigDecimal(100)).setScale(0).toString());
		request.setTotal_fee(fee);
		request.setRefund_fee(fee);
		request.setOp_user_id(request.getMch_id());
		request.sign();
		UnifiedRefundResponse resp = request.execute();
		System.out.println(resp.getReturn_msg());//====================
		if(!resp.getReturn_code().equalsIgnoreCase("SUCCESS")){
			throw new Exception("退款失败！"+resp.getReturn_msg());
		}
	}
	
	/**
	 * 支付宝退款请求
	 * @param ord
	 * @throws Exception
	 */
	private void alipayRefund(OrderRecord ord,String trade_no) throws Exception{
		AlipayRefundRequest r = new AlipayRefundRequest();
		r.initialize();
		r.setDetail_data(AlipayRefundRequest.detailFormatter(trade_no, ord.getFeeCash(), "refund"));
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		r.setBatch_no(sdf.format(d)+ord.getOrderNo().toString());
		sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
		r.setRefund_date(sdf.format(d));
		r.setBatch_num("1");
		r.sign("RSA");
		HashMap<String,String> res = r.execute();
		System.out.println(res.toString());//====================
		if(!res.get("is_success").equalsIgnoreCase("T")){
			throw new Exception("退款失败！"+res.toString());
		}
	}
	
	/**
	 * 检查订单状态
	 * @param ord
	 * @param status
	 * @return
	 */
	private boolean checkOrderStatus(OrderRecord ord,String status){
		return ord.getStatus().equals(status);
	}
	
	
	public void api(){
		renderJson(getRequest().getHeaderNames());
	}
}
