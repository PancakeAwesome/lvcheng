package com.lvoutcity.core.exception;

/** 
* @ClassName: BaseException 
* @Description: 异常基类
* @author Gdh
* @date 2016年4月25日 上午11:28:42 
*  
*/
public class BaseException extends RuntimeException{

	private static final long serialVersionUID = 1797547200683567838L;

	public BaseException(String message){
		super(message);
	}
	
	public BaseException(Throwable e){
		super(e);
	}
}
