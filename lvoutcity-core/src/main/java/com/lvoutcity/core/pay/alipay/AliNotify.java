package com.lvoutcity.core.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.lvoutcity.core.pay.RequestParamMapUtils;
import com.lvoutcity.core.pay.alipay.sign.RSA;

public class AliNotify {
	private String body;
	private String buyer_email;
	private String buyer_id;
	private String discount;
	private String gmt_close;
	private String gmt_create;
	private String gmt_payment;
	private String gmt_refund;
	private String is_total_fee_adjust;
	private String notify_id;
	private String notify_time;
	private String notify_type;
	private String out_trade_no;
	private String payment_type;
	private String price;
	private Integer quantity;
	private String refund_status;
	private String seller_email;
	private String seller_id;
	private String sign;
	private String sign_type;
	private String subject;
	private String total_fee;
	private String trade_no;
	private String trade_status;
	private String use_coupon;

	/**
	 * 验证签名
	 * @return
	 * @throws AlipayApiException
	 */
	public boolean checkSign() throws AlipayApiException{
		Prop prop = PropKit.use("alipay.props"); 
		String publicKey = prop.get("ali.publicKey");
		String _sign = this.getSign();
		String _sign_type = this.getSign_type();
		this.setSign(null);
		this.setSign_type(null);
		String paramsStr = RequestParamMapUtils.parseToParamStr(this);

		return AlipaySignature.rsaCheck(paramsStr.trim(), _sign.trim(), publicKey, "utf-8", _sign_type);
	}

	public String getBody() {
		return body;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public String getDiscount() {
		return discount;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public String getGmt_payment() {
		return gmt_payment;
	}

	public String getGmt_refund() {
		return gmt_refund;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public String getGmt_close() {
		return gmt_close;
	}

	public void setGmt_close(String gmt_close) {
		this.gmt_close = gmt_close;
	}

	public String getNotify_time() {
		return notify_time;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public String getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public String getSign() {
		return sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public String getSubject() {
		return subject;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public String getUse_coupon() {
		return use_coupon;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public void setGmt_refund(String gmt_refund) {
		this.gmt_refund = gmt_refund;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	
	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}
	
	/**
	 * 验证notifyId
	 * @return
	 */
	public boolean verify(){
		Prop prop = PropKit.use("alipay.props"); 
		String partnerId = prop.get("ali.partnerId"); // 合作商户id
		String notifyVerfiyRes = HttpKit
				.get(String.format("https://mapi.alipay.com/gateway.do?service=notify_verify&partner=%s&notify_id=%s",
						partnerId, this.getNotify_id()));

		return notifyVerfiyRes.trim().equalsIgnoreCase("true");
	}
	
	
	public static void main(String[] a) throws AlipayApiException{
		Prop prop = PropKit.use("alipay.props"); 
		String publicKey = prop.get("ali.publicKey");
		publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
		String paramsStr = "body=特别不好玩的一个线路哈哈哈哈&buyer_email=lucky518.student@sina.com&buyer_id=2088102025360191&discount=0.00&gmt_create=2016-06-24 14:00:00&gmt_payment=2016-06-24 14:00:01&gmt_refund=2016-06-24 14:03:02.470&is_total_fee_adjust=N&notify_id=240dc94c3e8eae8c1add2b95797ae36hgu&notify_time=2016-06-24 14:07:10&notify_type=trade_status_sync&out_trade_no=100000000298&payment_type=1&price=0.01&quantity=1&refund_status=REFUND_SUCCESS&seller_email=postmaster@lvoutcity.com&seller_id=2088221758502079&subject=大牛3号&total_fee=0.01&trade_no=2016062421001004190271330943&trade_status=TRADE_CLOSED&use_coupon=N";
		String _sign = "NZ15ncCfioDhDKEYmxN96DZcAwI4VN1ECEjT85542qfZ4VvHrLiJrnFEr9p6Zglu9dSA5wYdQsyiRTeicHa36ePGluhSoHBSA0OWxQrIARBh1qaUeGwgT9JuarK52OvWf4aWBKeA5BiL70IJFOl//nd193uKPFu4Ic0BZs8hNp8="; 
        System.out.println(
        		RSA.verify(paramsStr.trim(), _sign.trim(), publicKey, "utf-8")
        		);
	}
}
