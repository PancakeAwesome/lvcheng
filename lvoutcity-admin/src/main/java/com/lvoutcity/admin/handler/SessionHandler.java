/**
 * 
 */
package com.lvoutcity.admin.handler;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.core.util.Constants;

/**
 * @author wj
 *
 */
public class SessionHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		// TODO Auto-generated method stub
		// 遍历cookie
		Cookie[] co = request.getCookies();
		if(co!=null&&co.length>0){
			for (int i = 0; i < co.length; i++) {
				if (Constants.COOKIE_NAME.equals(co[i].getName())) {
					// 重设session 失效时间
					
					// 将redis 中session值赋值给当前session
					Map sessionMap = Redis.use().get(co[i].getValue());
					if (sessionMap != null) {
						Iterator iter = sessionMap.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							request.getSession().setAttribute(entry.getKey().toString(), entry.getValue());
							// Object key = entry.getKey();
							// Object val = entry.getValue();
						}
						// 重设session 失效时间
						Redis.use().setex(co[i].getValue(), Constants.SESSION_TIME,sessionMap);
					}

				}
			}
		}
		

		next.handle(target, request, response, isHandled);
	}

}
