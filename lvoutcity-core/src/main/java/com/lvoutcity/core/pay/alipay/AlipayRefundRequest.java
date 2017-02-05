package com.lvoutcity.core.pay.alipay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.lvoutcity.core.pay.RequestParamMapUtils;
import com.lvoutcity.core.pay.alipay.sign.RSA;

public class AlipayRefundRequest {
	String _input_charset;
	String	batch_no;
	String	batch_num;
	String	dback_notify_url;
	String	detail_data;
	String	notify_url;
	String	partner;
	String	refund_date;
	String	return_type;
	String	service;
	String	sign;
	String	sign_type;
	String	use_freeze_amount;

	
	public AlipayRefundRequest(){
		
	}
	
	public void initialize(){
		Prop prop = PropKit.use("alipay.props"); 
		this.partner = prop.get("ali.partnerId");
		this.service="refund_fastpay_by_platform_nopwd";
		this._input_charset="utf-8";
		this.notify_url=prop.get("ali.refundNotifyUrl");
	}
	
	public String getService() {
		return service;
	}



	public void setService(String service) {
		this.service = service;
	}


	public String get_input_charset() {
		return _input_charset;
	}


	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}
	public String getPartner() {
		return partner;
	}



	public void setPartner(String partner) {
		this.partner = partner;
	}





	public String getSign_type() {
		return sign_type;
	}



	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}



	public String getSign() {
		return sign;
	}



	public void setSign(String sign) {
		this.sign = sign;
	}



	public String getNotify_url() {
		return notify_url;
	}



	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}



	public String getDback_notify_url() {
		return dback_notify_url;
	}



	public void setDback_notify_url(String dback_notify_url) {
		this.dback_notify_url = dback_notify_url;
	}



	public String getBatch_no() {
		return batch_no;
	}



	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}



	public String getRefund_date() {
		return refund_date;
	}



	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
	}



	public String getBatch_num() {
		return batch_num;
	}



	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}



	public String getDetail_data() {
		return detail_data;
	}



	public void setDetail_data(String detail_data) {
		this.detail_data = detail_data;
	}



	public String getUse_freeze_amount() {
		return use_freeze_amount;
	}



	public void setUse_freeze_amount(String use_freeze_amount) {
		this.use_freeze_amount = use_freeze_amount;
	}



	public String getReturn_type() {
		return return_type;
	}



	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}

	public void generateBatchNo(){
		
	}
	
	public static String detailFormatter(String tradeNo,BigDecimal fee, String reason){
		String f = fee.setScale(2).toString();
		return tradeNo+"^"+f+"^"+reason;
	} 
	
	public void sign(String type) throws Exception {
		Prop prop = PropKit.use("alipay.props");
		if (type.equalsIgnoreCase("md5")) {
			String sstr = RequestParamMapUtils.parseToParamStr(this) + prop.get("ali.key");
			this.setSign(HashKit.md5(sstr));
			this.setSign_type("MD5");
		} else if (type.equalsIgnoreCase("rsa")) {
/*			this.setSign(AlipaySignature.rsaSign(RequestParamMapUtils.parseToParamStr(this), prop.get("ali.privateKey"),
					"utf-8"));*/
			System.out.println(RequestParamMapUtils.parseToParamStr(this));
			this.setSign(URLEncoder.encode(RSA.sign(RequestParamMapUtils.parseToParamStr(this),prop.get("ali.privateKey").trim(), "utf-8"),"utf-8"));
			System.out.println(this.getSign());
			this.setSign_type("RSA");
		}
	}
	
	
	/**
	 * @return {is_success=F, error=ILLEGAL_SIGN}  
	 * @throws Exception
	 */
	public HashMap<String,String> execute() throws Exception{
		XmlMapper mapper = new XmlMapper();
		HttpKit.setCharSet("utf-8");
		System.out.println(RequestParamMapUtils.parseToParamStr(this));
//		String res = HttpKit.get("https://mapi.alipay.com/gateway.do?_input_charset=utf-8",  RequestParamMapUtils.parseToParamMap(this));
		String res = HttpKit.post("https://mapi.alipay.com/gateway.do?_input_charset=utf-8",  RequestParamMapUtils.parseToParamStr(this));
		HashMap<String,String> response = mapper.readValue(res, HashMap.class);
		return 	response;
	} 

    
    
	public static void main(String[] args){
		try {
			AlipayRefundRequest r = new AlipayRefundRequest();
			r.initialize();
			r.setDetail_data("2016062421001004190259223335^0.01^good");
			r.setBatch_no("20160624"+"100000000296");
			r.setRefund_date("2016-06-24 11:48:00");
			r.setBatch_num("1");
			r.sign("RSA");
			System.out.println(r.execute().toString());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
