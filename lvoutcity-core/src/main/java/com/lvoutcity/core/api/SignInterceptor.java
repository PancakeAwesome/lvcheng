package com.lvoutcity.core.api;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.kit.HashKit;
import com.lvoutcity.core.api.Restful.Method;

/**
 * Sign拦截器
 * 
 * @author gdh
 *
 */
public class SignInterceptor extends PrototypeInterceptor {

	@Override
	public void doIntercept(Invocation inv) {
		
		if(inv.getMethod().getAnnotation(NeedSign.class) == null){
			inv.invoke();
			return;
		}
		
		if(!inv.getController().getRequest().getMethod().toLowerCase().equals(Method.Post.name().toLowerCase())){
			inv.getController().renderJson(APIRet.unsupportMethod());
			return;
		}
		
		if (inv.getMethod().getAnnotation(NeedSign.class) == null) {
			inv.invoke();
			return;
		}

		String sign = inv.getController().getRequest().getHeader(Constant.SIGN_KEY);
		String token = inv.getController().getRequest().getHeader(Constant.TOKEN_KEY);
		if (StringUtils.isEmpty(sign)) {
			inv.getController().renderJson(APIRet.signFail());
			return;
		}
		if (StringUtils.isEmpty(token)) {
			inv.getController().renderJson(APIRet.tokenFail());
			return;
		}

		String body = inv.getController().getAttrForStr(Constant.BODY_KEY);

		String _sign = HashKit.md5(HashKit.md5(body) + token);

		if (!_sign.equals(sign)) {
			inv.getController().renderJson(APIRet.signFail());
			return;
		}

		inv.getController().setAttr(Constant.BODY_KEY, body);
		inv.invoke();

	}

}
