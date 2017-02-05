package com.lvoutcity.core.api;

/**
 * Api接口输出参数封装
 * 
 * @author gdh
 *
 */
public final class APIRet<T> {

	private Integer code;
	private String msg;
	private T data;

	private APIRet() {

	}

	/**
	 * token验证失败
	 * 
	 * @param msg
	 * @return
	 */
	public static <T> APIRet<T> tokenFail() {
		return tokenFail("请求头缺少Token参数");
	}
	
	public static <T> APIRet<T> tokenFail(String msg) {
		return create(Constant.TOKEN_FAIL, msg, null);
	}

	/**
	 * 签名认证失败
	 * 
	 * @param msg
	 * @return
	 */
	public static <T> APIRet<T> signFail() {
		return create(Constant.SIGN_FAIL, "签名不匹配", null);
	}
	
	public static <T> APIRet<T> signFail(String msg) {
		return create(Constant.SIGN_FAIL, msg, null);
	}
	
	public static <T>APIRet<T> signEmpty(){
		return create(Constant.SIGN_FAIL, "签名为空", null);
	}
	
	public static <T>APIRet<T> unsupportMethod(){
		return create(Constant.SIGN_FAIL, "不支持此请求方法", null);
	}
	

	/**
	 * 操作成功
	 * 
	 * @param data
	 * @return
	 */
	public static <T> APIRet<T> success(T data) {
		return create(Constant.BIZ_SUCCESS, "操作成功", data);
	}

	/**
	 * 操作成功
	 * 
	 * @param msg
	 * @param data
	 * @return
	 */
	public static <T> APIRet<T> success(String msg, T data) {
		return create(Constant.BIZ_SUCCESS, msg, data);
	}

	/**
	 * 操作失败
	 * 
	 * @param msg
	 * @param data
	 * @return
	 */
	public static <T> APIRet<T> fail(String msg, T data) {
		return create(Constant.BIZ_FAIL, msg, data);
	}

	/**
	 * 操作失败
	 * 
	 * @param data
	 * @return
	 */
	public static <T> APIRet<T> fail(T data) {
		return create(Constant.BIZ_FAIL, "操作失败", data);
	}

	private static <T> APIRet<T> create(Integer code, String msg, T data) {
		return new APIRet<T>().setData(data).setMsg(msg).setCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public APIRet<T> setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public APIRet<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public APIRet<T> setData(T data) {
		this.data = data;
		return this;
	}
}
