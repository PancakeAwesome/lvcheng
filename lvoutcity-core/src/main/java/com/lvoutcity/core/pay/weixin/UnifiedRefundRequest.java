package com.lvoutcity.core.pay.weixin;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.lvoutcity.core.pay.RequestParamMapUtils;
@JacksonXmlRootElement(localName = "xml")
public class UnifiedRefundRequest {
	
	private String appid;
	private String device_info;
	private String mch_id;
	private String nonce_str;
	private String op_user_id;
	private String out_refund_no;
	private String out_trade_no;
	private Integer refund_fee;
	private String refund_fee_type;
	private String sign;
	private Integer total_fee;
	private String transaction_id;

	
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
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

	public String getOp_user_id() {
		return op_user_id;
	}

	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
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

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
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

	// 签名
	public void sign() throws Exception {
		Prop prop = PropKit.use("wxpay.props");
		this.sign = SignUtils.getSign(this, prop.get("wx.key"));
	}

	/**
	 * 生成随机字符串
	 */
	public void generateNonceStr() {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 32; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		this.nonce_str = sb.toString();
	}

	/**
	 * 执行退款请求
	 * @return UnifiedOrderResponse
	 * @throws Exception
	 */
	public UnifiedRefundResponse execute() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream instream =  this.getClass().getResourceAsStream("/apiclient_cert.p12");
		try {
			keyStore.load(instream, this.getMch_id().toCharArray());
		} finally {
			instream.close();
		}
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, this.getMch_id().toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {

			HttpPost http_post = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
			// 转换参数
			XmlMapper mapper = new XmlMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			System.out.println(mapper.writeValueAsString(this));
			ByteArrayEntity params = new ByteArrayEntity(mapper.writeValueAsString(this).getBytes());
			http_post.setEntity(params);
			
			System.out.println("executing request " + http_post.getRequestLine());
			// 执行
			CloseableHttpResponse response = httpclient.execute(http_post);
			try {
				
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					InputStream is = entity.getContent();
					byte[] resByte = new byte[(int) entity.getContentLength()];
					is.read(resByte);
					String res = new String(resByte);
					UnifiedRefundResponse resp = mapper.readValue(res, UnifiedRefundResponse.class);
					return resp;
				}
				EntityUtils.consume(entity);
				return null;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
	
	public UnifiedRefundRequest initialize() {
		Prop prop = PropKit.use("wxpay.props");
		this.appid = prop.get("wx.appId");
		this.mch_id = prop.get("wx.mchId");
		
		return this;
	}

	public final static void main(String[] args) {
		try {
			UnifiedRefundRequest request = new UnifiedRefundRequest();
			request.initialize();
			request.generateNonceStr();
			request.setOut_trade_no("100000000166");
			request.setOut_refund_no("100000000166");
			request.setTotal_fee(1);
			request.setRefund_fee(1);
			request.setOp_user_id(request.getMch_id());
			request.sign();
			System.out.println(RequestParamMapUtils.parseToParamMap(request.execute()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
