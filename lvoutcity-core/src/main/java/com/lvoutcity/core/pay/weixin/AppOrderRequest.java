package com.lvoutcity.core.pay.weixin;

import java.util.Random;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/** 
 * @ClassName: UnifiedOrderRequest 
 * @Description: 统一下单请求
 * @author 管东海
 *  
 */
@JacksonXmlRootElement(localName = "xml")
public class AppOrderRequest {

	private String appid;
	private String noncestr;
	private String package_;
	private String partnerid;
	private String prepayid;
	private String sign;
	private String timestamp;
	
	/**
	 * 生成随机字符串
	 */
	public void generateNonceStr(){
		    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
		    Random random = new Random();     
		    StringBuffer sb = new StringBuffer();     
		    for (int i = 0; i < 32; i++) {     
		        int number = random.nextInt(base.length());     
		        sb.append(base.charAt(number));     
		    }     
		    this.noncestr=sb.toString();     
	}


	public String getAppid() {
		return appid;
	}


	public String getNoncestr() {
		return noncestr;
	}


	public String getPackage_() {
		return package_;
	}


	public String getPartnerid() {
		return partnerid;
	}
	
	public String getPrepayid() {
		return prepayid;
	}

	public String getSign() {
		return sign;
	}

	

	public String getTimestamp() {
		return timestamp;
	}

	public void initialize(){
		Prop prop = PropKit.use("wxpay.props"); 
		this.appid = prop.get("wx.appId");
		this.partnerid = prop.get("wx.mchId");
	}

	
	
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	} 

	
	public void setPackage_(String package_) {
		this.package_ = package_;
	}


	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}


	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	// 签名
	public void sign() throws Exception{
		Prop prop = PropKit.use("wxpay.props"); 
		this.sign = SignUtils.getSign(this, prop.get("wx.key"));
	}
}
