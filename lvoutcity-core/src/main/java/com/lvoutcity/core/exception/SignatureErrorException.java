package com.lvoutcity.core.exception;

/** 
* @ClassName: SignatureErrorException 
* @Description: 签名错误异常
* @author Gdh
* @date 2016年4月25日 上午11:29:59 
*  
*/
public class SignatureErrorException extends BaseException{

	private static final long serialVersionUID = -4367020370284604784L;
	
	public SignatureErrorException(String message) {
		super(message);
	}
	
}
