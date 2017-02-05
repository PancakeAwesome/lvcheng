package com.lvoutcity.core.pay.weixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnifiedRefundResponse {
	
	private String appid;// 微信开放平台审核通过的应用APPID
	private Integer cash_fee;// 现金支付金额，单位为分，只能为整数，详见支付金额
	private Integer cash_refund_fee;// 现金退款金额，单位为分，只能为整数，详见支付金额
	private Integer coupon_refund_count;// 代金券或立减优惠使用数量
	private Integer coupon_refund_fee;// 代金券或立减优惠退款金额=订单金额-现金退款金额，注意：立减优惠金额不会退回
	private String coupon_refund_id;// 代金券或立减优惠ID
	private String device_info;// 微信支付分配的终端设备号，与下单一致
	private String err_code;// 列表详见错误码列表
	private String err_code_des;// 结果信息描述
	private String fee_type;// 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private String mch_id;// 微信支付分配的商户号
	private String nonce_str;// 随机字符串，不长于32位
	private String out_refund_no;// 商户退款单号
	private String out_trade_no;// 商户系统内部的订单号
	private String refund_channel;// ORIGINAL—原路退款
	private Integer refund_fee;// 退款总金额,单位为分,可以做部分退款
	private String refund_id;// 微信退款单号
	private String result_code;// SUCCESS/FAIL
	private String return_code;// SUCCESS/FAIL
	private String return_msg;// 返回信息，如非空，为错误原因
	private String sign;// 签名，详见签名算法
	private Integer total_fee;// 订单总金额，单位为分，只能为整数，详见支付金额
	private String transaction_id;// 微信订单号
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public Integer getCash_fee() {
		return cash_fee;
	}
	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}
	public Integer getCash_refund_fee() {
		return cash_refund_fee;
	}
	public void setCash_refund_fee(Integer cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}
	public Integer getCoupon_refund_count() {
		return coupon_refund_count;
	}
	public void setCoupon_refund_count(Integer coupon_refund_count) {
		this.coupon_refund_count = coupon_refund_count;
	}
	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}
	public void setCoupon_refund_fee(Integer coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}
	public String getCoupon_refund_id() {
		return coupon_refund_id;
	}
	public void setCoupon_refund_id(String coupon_refund_id) {
		this.coupon_refund_id = coupon_refund_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getRefund_channel() {
		return refund_channel;
	}
	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}
	public Integer getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Integer getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	
	

}
