package com.lvoutcity.core.pay.weixin;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/** 
 * @ClassName: UnifiedOrderRequest 
 * @Description: 统一下单请求
 * @author 管东海
 *  
 */
public class UnifiedOrderRequest {

	private String appid;
	private String attach;
	private String body;
	private String mch_id;
	private String nonce_str;
	private String notify_url;
	private String out_trade_no;
	private String partnerid;
	private String prepay_id;
	private String spbill_create_ip;
	private String timestamp;
	private String total_fee;
	private String trade_type = "APP";
	private String sign;
	


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public void initialize(){
		Prop prop = PropKit.use("wxpay.props"); 
		this.appid = prop.get("wx.appId");
		this.mch_id = prop.get("wx.mchId");
		this.notify_url=prop.get("wx.notifyUrl");
	}
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
	
	// 签名
	public void sign() throws Exception{
		Prop prop = PropKit.use("wxpay.props"); 
		this.sign = SignUtils.getSign(this, prop.get("wx.key"));
	}
	
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
		    this.nonce_str=sb.toString();     
	} 

	public UnifiedOrderResponse requestPrepay() throws Exception {
		Prop prop = PropKit.use("wxpay.props");
		XmlMapper mapper = new XmlMapper();

		mapper.setSerializationInclusion(Include.NON_NULL);
		URL url = new URL(prop.get("wx.unifiedOrderUrl"));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);

		conn.setDoInput(true);
		System.out.println(mapper.writeValueAsString(this));
		conn.getOutputStream().write(mapper.writeValueAsString(this).getBytes());

		byte[] resByte = new byte[conn.getInputStream().available()];

		conn.getInputStream().read(resByte);

		String res = new String(resByte);
		System.out.println(res);

		UnifiedOrderResponse resp = mapper.readValue(res, UnifiedOrderResponse.class);
		System.out.println(resp);

		return resp;

	}
	
	public static void main(String[] args) throws Exception {
		UnifiedOrderRequest uo = new UnifiedOrderRequest();

		uo.setAppid("wxfe9c30e52fceff61");
		uo.setBody("商品描述");
		uo.setMch_id("1348147101");
		uo.setNonce_str("13232323232");
		uo.setNotify_url("http://www.digirun.cn");
		uo.setOut_trade_no("1100000004334");
		uo.setSpbill_create_ip("218.94.67.189");
		uo.setTotal_fee("1");
		String sign = SignUtils.getSign(uo, "6vvB4QzOtU6Ulr4wjhYbwLGQQGryTut1");
		uo.setSign(sign);
		XmlMapper mapper = new XmlMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.getOutputStream().write(mapper.writeValueAsString(uo).getBytes());
		byte [] resByte = new byte[conn.getInputStream().available()];
		conn.getInputStream().read(resByte);
		String res = new String(resByte);
		System.out.println(res);
		UnifiedOrderResponse resp = mapper.readValue(res, UnifiedOrderResponse.class);
		System.out.println(resp);
	}


	public String getPartnerid() {
		return partnerid;
	}


	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
}
