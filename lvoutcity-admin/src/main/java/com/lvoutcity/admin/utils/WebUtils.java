package com.lvoutcity.admin.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import static com.lvoutcity.core.util.LvoutcityUtils.isNull;

import java.util.Iterator;
import java.util.Map;

import static com.lvoutcity.core.util.LvoutcityUtils.isNotNull;

import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;

/**
 * 
 * @ClassName: WebUtils 
 * @Description: Web帮助类
 * @author James w.s
 * @date 2016年5月18日 下午5:21:16 
 *
 */
public class WebUtils {

	/**
	 * 
	 * @Title: getUser 
	 * @Description: 获取登录用户 
	 * @param @param request
	 * @param @return    设定文件 
	 * @return User    返回类型 
	 * @create_time 2016下午5:35:22
	 * @create_user James w.s
	 * @throws
	 */
	public static User getUser1(HttpServletRequest request){
		return (User) request.getSession().getAttribute(Constants.SYS_USER);
	}
	
	/**
	 * 
	 * @Title: getUserBack 
	 * @Description: 获取登录用户基本信息
	 * @param @param request
	 * @param @return    设定文件 
	 * @return UserBackgroup    返回类型 
	 * @create_time 2016 2016年6月27日 下午8:13:00
	 * @create_user James w.s
	 * @throws
	 */
	public static UserBackgroup getUserBack3(HttpServletRequest request){
		return (UserBackgroup) request.getSession().getAttribute(Constants.SYS_USER_BACK);
	}
	
//	public static HttpServletRequest(){
//	}
//	
	
	/**
	 * 
	 * @Title: getUser 
	 * @Description: 获取登录用户
	 * @param @param session1
	 * @param @return    设定文件 
	 * @return User    返回类型 
	 * @create_time 2016下午5:36:29
	 * @create_user James w.s
	 * @throws
	 */
	public static User getUser2(HttpSession session){
		return (User) session.getAttribute(Constants.SYS_USER);
	}
	
	/**
	 * 
	 * @Title: logout 
	 * @Description: 退出登录 
	 * @param @param request    设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午4:03:53
	 * @create_user James w.s
	 * @throws
	 */
	public static void logout2(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(isNotNull(session)){
			session.invalidate();
		}
		
	}
	
	/**
	 * 
	 * @Title: getPermission 
	 * @Description: 获取权限
	 * @param @return    设定文件 
	 * @return PermissionBean    返回类型 
	 * @create_time 2016下午5:38:30
	 * @create_user James w.s
	 * @throws
	 */
	public static PermissionBean getPermission(HttpServletRequest request){
		String name = "";
		Cookie[] co = request.getCookies();
		for (int i = 0; i < co.length; i++) {
			if (Constants.COOKIE_NAME.equals(co[i].getName())) {
				name = co[i].getValue();
				break;
			}
		}
		return (PermissionBean) ((Map)Redis.use().get(name)).get(Constants.DEFAULT_PERMISSION);
	}
	
/*	public static boolean LoginOut2(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(isNull(session) || session.isNew() || isNull(WebUtils.getUser(request))){
			return true;
		}
		return false;
	}*/
}
