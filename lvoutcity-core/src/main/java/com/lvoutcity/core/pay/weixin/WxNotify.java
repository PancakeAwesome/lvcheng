package com.lvoutcity.core.pay.weixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WxNotify {
	private String appid;
	private String attach;
	private String bank_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private String err_code;
	private String err_code_des;
	private String fee_type;
	private String out_trade_no;
	private String result_code;
	private String return_code;
	private String return_msg;
	private String time_end;
	private String mch_id;
	private Integer total_fee;
	private String is_subscribe;

	public String getAttach() {
		return attach;
	}

	public String getBank_type() {
		return bank_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public String getErr_code() {
		return err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public String getFee_type() {
		return fee_type;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public String getResult_code() {
		return result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public String getTime_end() {
		return time_end;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
}
