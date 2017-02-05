package com.lvoutcity.core.api;

import java.io.InputStream;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.lvoutcity.core.api.Restful.Method;
import com.lvoutcity.core.api.Restful.RequestMethod;

/**
 * Rest 拦截器
 * 
 * @author gdh
 *
 */
public class RestfulInterceptor extends PrototypeInterceptor {

	@Override
	public void doIntercept(Invocation inv) {
		String method = inv.getController().getRequest().getMethod().toLowerCase();
		RequestMethod requestMethod = inv.getMethod().getAnnotation(RequestMethod.class);
		if (requestMethod == null) {
			inv.getController().renderJson(APIRet.unsupportMethod());
			return;
		}
		
		Method[] methods = requestMethod.value();
		boolean flag = false;
		for (Method m : methods) {
			if (m.name().toString().toLowerCase().equals(method)) {
				flag = true;
				break;
			}
		}

		if (flag) {
			if (method.equals(Method.Post.name().toLowerCase())) { // post
				inv.getController().getRequest().getParameterMap();
				String body = null;

				try {
					InputStream in = inv.getController().getRequest().getInputStream();
					byte[] buffer = new byte[inv.getController().getRequest().getContentLength()];
					in.read(buffer);
					body = new String(buffer);
				} catch (Exception e) {
					inv.getController().renderJson(APIRet.signFail("无法解析body"));
					return;
				}
				inv.getController().setAttr(Constant.BODY_KEY, body);
			}

			inv.invoke();
			return;
		}
		inv.getController().renderJson(APIRet.unsupportMethod());
	}

}
