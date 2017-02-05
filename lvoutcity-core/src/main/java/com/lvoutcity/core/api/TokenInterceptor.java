package com.lvoutcity.core.api;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.plugin.redis.Redis;

/**
 * Token拦截器
 * 
 * @author gdh
 *
 */
public class TokenInterceptor extends PrototypeInterceptor {

	@Override
	public void doIntercept(Invocation inv) {
		if (inv.getMethod().getAnnotation(NeedToken.class) == null) {
			inv.invoke();
			return;
		}

		APIRet<Object> tokenFail = APIRet.tokenFail("登陆信息已过期，请重新登陆");
		
		
		String token = inv.getController().getRequest().getHeader(Constant.TOKEN_KEY);
		if (StringUtils.isEmpty(token)) { // 非法
			inv.getController().renderJson(tokenFail);
			return;
		}
		
		Object tmpObj = Redis.use().hget("LoginTokens", token);
		if(tmpObj == null){
			inv.getController().renderJson(tokenFail);
			return;
		}
		TokenBean tokenBean = (TokenBean)tmpObj;
		if(new Date().compareTo(tokenBean.getExpire()) == 1){
			inv.getController().renderJson(tokenFail);
			return;
		}
		inv.getController().setAttr(Constant.TokenBean, tokenBean);
		inv.invoke();
	}

}
