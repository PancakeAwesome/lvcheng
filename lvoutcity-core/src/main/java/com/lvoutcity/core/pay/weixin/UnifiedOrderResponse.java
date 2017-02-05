package com.lvoutcity.core.pay.weixin;

/** 
 * @ClassName: UnifiedOrderResponse 
 * @Description: 统一下单响应
 * @author 管东海
 *  
 */
public class UnifiedOrderResponse extends UnifiedOrderRequest{

	public UnifiedOrderResponse() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	private String return_code;
	private String return_msg;
	private String prepay_id;
	private String result_code;
	private String err_code;
	private String err_code_des;

	

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

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
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

	@Override
	public String toString() {
		return "UnifiedOrderResponse [return_code=" + return_code + ", return_msg=" + return_msg + ", prepay_id="
				+ prepay_id + ", result_code=" + result_code + ", err_code=" + err_code + ", err_code_des="
				+ err_code_des + "]";
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
