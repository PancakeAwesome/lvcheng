package com.lvoutcity.admin.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.render.CaptchaRender;
import com.lvoutcity.admin.Interceptor.LoginInterceptor;
import com.lvoutcity.admin.utils.CaptchaRewriteRender;
import com.lvoutcity.admin.utils.PermissionBean;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.SysMenu;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;

import redis.clients.jedis.Jedis;

/**
 * 
 * @ClassName: LoginController 
 * @Description: 用户登录
 * @author James w.s
 * @date 2016年6月16日 下午6:31:04 
 *
 */
@Clear(LoginInterceptor.class)
public class LoginController extends BaseController {
	
	
	@Before(POST.class)
	public void login() {
		// 添加用户登录session
		
		try {
			String user = getPara("userName");
			String pd = getPara("password");
			String vaildCode = getPara("validCode");
			if(LvoutcityUtils.isNull(user)){
				renderJson("result","用户名为空");
				//render("../login.html");
				return;
			}
			if(LvoutcityUtils.isNull(pd)){
				renderJson("result","密码为空");
				//render("../login.html");
				return;
			}
			if(LvoutcityUtils.isNull(vaildCode)){
				renderJson("result","验证码为空");
				//render("../login.html");
				return;
			}
			if(!CaptchaRewriteRender.validate(this, vaildCode)){
				renderJson("result","验证码错误");
				//render("../login.html");
				return;
			}
			UserBackgroup ub = UserBackgroup.dao.findFirst("select * from t_user_backgroup where  user_name ='" + user + "' ");
			if(LvoutcityUtils.isNull(ub)){
				renderJson("result","用户不存在");
				//render("../login.html");
				return;
			}
			//尝试次数过多冻结10分钟
			Jedis jedis = Redis.use().getJedis();
			String key = "login_count_"+ub.getUserId();
			System.out.println("key: "+key+" value:"+ String.valueOf(jedis.get(key)));
			if(jedis.get(key) != null && Integer.valueOf(jedis.get(key)).compareTo(5)>=0){
				renderJson("result","您尝试次数过多，请稍后再试！");
				return;
			}
			if(!LvoutcityUtils.encryptPassword(pd).equals(ub.getPassword())){
				//密码错误了
				jedis.incr(key);
				jedis.expire(key, 600); //10分钟
				renderJson("result","密码错误!");
				//render("../login.html");
				return;
			}
			//登陆成功时清除掉记录
			jedis.del(key);
			if(!"1".equals(ub.getStatus())){
				renderJson("result","账号被禁用");
				//render("../login.html");
				return;
			}
			String clubName = " ";
			TourismClub cc = TourismClub.dao.findById(ub.getClubId());
			if(cc!=null){
				clubName = cc.getClubShortName();
			}
			// 判断是否属于旅城
			if(!Constants.LVC_CLUB_ID.equals(ub.getClubId())){
				// 俱乐部是否启用
				//TourismClub cc = TourismClub.dao.findById(ub.getClubId());
				if(!"1".equals(cc.getIsDelete())){
					renderJson("result","所属俱乐部被删除");
					//render("../login.html");
					return;
				}
				if(!"1".equals(cc.getEnabled())){
					renderJson("result","所属俱乐部被禁用");
					//render("../login.html");
					return;
				}
				clubName = cc.getClubShortName();
			}
			
            
			User uu = User.dao.findById(ub.getUserId());
	//########登录踢人代码#######		
/*			// 判断是否已经登录，如果登录踢出账号
			// 失效
			Map tt = Redis.use().hgetAll(LvoutcityUtils.encryptPassword(uu.getId()));
			if(tt!=null){
				for (Object key:tt.keySet()) {
					//  删除多余的session
					Redis.use().del(tt.get(key));
					//如果存在 ,全部删除
					 Redis.use().hdel(LvoutcityUtils.encryptPassword(uu.getId()),key);
					  // System.out.println("key= "+ key.toString() + " and value= " + map.get(key));
					  }
			}
			
		    // 设置是否登录表示,将id加密后存入redis
            Redis.use().hset(LvoutcityUtils.encryptPassword(uu.getId()), getCookie(Constants.COOKIE_NAME), getCookie(Constants.COOKIE_NAME));     
  */           
//			Map  sessionMap = new HashMap();
//			sessionMap.put("user_back", ub);
//			sessionMap.put("current_user_id", uu.getId());
//			sessionMap.put(Constants.SYS_USER, uu);
//			sessionMap.put(Constants.SYS_USER_BACK,ub);
//			sessionMap.put("user", uu);
			setSessionAttr("user_back", ub);
			setSessionAttr("club_name", clubName);
			setSessionAttr("current_user_id", uu.getId());
			setSessionAttr(Constants.SYS_USER, uu);
			setSessionAttr(Constants.SYS_USER_BACK,ub);
			setSessionAttr("user", uu);
			if (LvoutcityUtils.isNull(ub.getClubId())) {
//			render("../error/403.html");
//			return;
			} else {
				setSessionAttr("clubId", ub.getClubId());
//				sessionMap.put("clubId", ub.getClubId());
			}
			try {
				initPermission(getRequest(),getResponse(),uu);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			renderJson("result",Constants.SUCCESS_CODE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public void loginout(){
		User user = WebUtils.getUser(getRequest());
		if(LvoutcityUtils.isNull(user)){
			//render(render);("");
		}
		WebUtils.logout(getRequest());
	}*/
	
	
	/**
	 * 注销session
	 */
	public void loginOut() throws Exception{
		// 移除session
		removeSessionAttr("user_back");
		removeSessionAttr("current_user_id");
		removeSessionAttr("user");
		removeSessionAttr("clubId");
		removeSessionAttr(Constants.SYS_USER);
		removeSessionAttr(Constants.SYS_USER_BACK);
		
		 Cookie idCookie = new Cookie(Constants.COOKIE_NAME, LvoutcityUtils.getUUID()); //可以使用md5或着自己的加密算法保存     
         idCookie.setPath("/"); //cookie路径问题，在我的其他文章里有专门的讲解     
         //idCookie.setMaxAge(MAX_AGE);     
         
         getResponse().addCookie(idCookie);
         renderJson("result","success");
		/*setSessionAttr("user_back", ub);
		setSessionAttr("current_user_id", uu.getId());
		setSessionAttr("user", uu);
		setSessionAttr("clubId", ub.getClubId());*/
	}
	
	/**
	 * 
	 * @Title: initPermission 
	 * @Description: 初始权限
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @param user
	 * @param @throws BaseException    设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午3:34:41
	 * @create_user James w.s
	 * @throws
	 */
	private void initPermission(HttpServletRequest request, HttpServletResponse response,
			User user) throws Exception{
		PermissionBean perm = new PermissionBean();
		if(Constants.ADMIN_ID.equals(user.getId())){
			perm.setPermissionMenus(SysMenu.dao.queryAllMenuTreeNode(user));
		}
		else{
			List<ZTree> list = SysMenu.dao.getPermissionMenuTreeNode(user);
			perm.setPermissionMenus(list);	//按钮
			perm.setPermMenus(list);		//菜单
		}	
		perm.setPermissionRoles(SysMenu.dao.getPermissionRoles(user));
		//perm.setLoginArchitectures(permissionService.getLoginArchitecture(user));
		setSessionAttr(Constants.DEFAULT_PERMISSION, perm);
//		HttpSession session = request.getSession();
//		if(LvoutcityUtils.isNotNull(session)){
//			session.setAttribute(Constants.DEFAULT_PERMISSION, perm);
//		}
	}
	
	
	
}
