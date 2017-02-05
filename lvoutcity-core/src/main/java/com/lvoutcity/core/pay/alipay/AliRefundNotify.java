package com.lvoutcity.core.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.lvoutcity.core.pay.RequestParamMapUtils;

public class AliRefundNotify {
	String batch_no;
	String notify_id;
	String notify_time;
	String notify_type;
	String result_details;
	String sign;
	String sign_type;
	String success_num;
	String unfreezed_details;

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	
	/*
	 * result_details格式为：第一笔交易#第二笔交易#第三笔交易...#第N笔交易。  
	 * 第 N 笔交易格式为：
	 * 交易退款数据集$收费退款数据集|分润退款数据集|分润退款数据集|...|分润退款数据集$$退子交易 
	 * 说明：
	 * “交易退款数据集”不可为空，其余数据皆可为空。 交易退款数据集格式为：原付款支付宝交易号^退款总金额^处理结果码。
	 */
	public String getResult_details() {
		return result_details;
	}

	public void setResult_details(String result_details) {
		this.result_details = result_details;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(String success_num) {
		this.success_num = success_num;
	}

	public String getUnfreezed_details() {
		return unfreezed_details;
	}

	public void setUnfreezed_details(String unfreezed_details) {
		this.unfreezed_details = unfreezed_details;
	}

	/**
	 * 验证签名
	 * 
	 * @return
	 * @throws AlipayApiException
	 */
	public boolean checkSign() throws AlipayApiException {
		Prop prop = PropKit.use("alipay.props");
		String publicKey = prop.get("ali.publicKey");
		String _sign = this.getSign();
		String _sign_type = this.getSign_type();
		this.setSign(null);
		this.setSign_type(null);
		return AlipaySignature.rsaCheck(RequestParamMapUtils.parseToParamStr(this), _sign, publicKey, "utf-8",
				_sign_type);
	}

	/**
	 * 验证notifyId
	 * 
	 * @return
	 */
	public boolean verify() {
		Prop prop = PropKit.use("alipay.props");
		String partnerId = prop.get("ali.partnerId"); // 合作商户id
		String notifyVerfiyRes = HttpKit
				.get(String.format("https://mapi.alipay.com/gateway.do?service=notify_verify&partner=%s&notify_id=%s",
						partnerId, this.getNotify_id()));

		return notifyVerfiyRes.trim().equalsIgnoreCase("true");
	}
	
	public String[] getDetail(){
		String str = this.getResult_details();
		String first = str.split("#")[0];
		String detail = first.split("$")[0];
		return detail.split("\\^");
	}

}
