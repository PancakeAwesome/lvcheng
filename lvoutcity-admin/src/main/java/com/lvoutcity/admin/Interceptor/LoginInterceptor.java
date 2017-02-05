package com.lvoutcity.admin.Interceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		// 向freemarker中传jsp的内置对象
		Controller controller = inv.getController();
		HttpServletRequest request = controller.getRequest();
		HttpSession session = controller.getSession();
		/*
		 * Prop prop = PropKit.use("config.props"); String
		 * url="http://localhost:8080";
		 * if(!LvoutcityUtils.isNull(prop.get("http.url"))){ url =
		 * prop.get("http.url"); } url += request.getContextPath();
		 */
		if (LvoutcityUtils.isNull(controller.getCookie(Constants.COOKIE_NAME))) {
			controller.renderHtml("<script>top.location='" + request.getAttribute("basePath") + "login'</script>");
		} else {
			Map sessionMap = Redis.use().get(controller.getCookie(Constants.COOKIE_NAME).toString());
			if (LvoutcityUtils.isNull(sessionMap) || LvoutcityUtils.isNull(sessionMap.get("current_user_id"))) {
				controller.renderHtml("<script>top.location='" + request.getAttribute("basePath") + "login'</script>");
			} else {
				inv.invoke();
			}
		}

	}

}
