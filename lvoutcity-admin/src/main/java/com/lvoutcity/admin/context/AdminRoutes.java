package com.lvoutcity.admin.context;

import com.jfinal.config.Routes;
import com.lvoutcity.admin.controller.*;

/** 
* @ClassName: AdminRoutes 
* @Description: 
* @author Gdh
* @date 2016年4月25日 下午12:52:39 
*  
*/
public class AdminRoutes extends Routes{

	@Override
	public void config() {

		add("/", IndexController.class);
		add("/common", CommonController.class);
		add("/setting", SettingController.class);
		add("/club", ClubController.class);
		add("/attr", AttractionsController.class);
		add("/member",MemberController.class);
		add("/userManager",UserController.class);
		add("/roleManager",RoleController.class);
		add("/app",AppController.class);
		add("/guide", GuideController.class);
		add("/picture", PictureController.class);
		add("/routeManager",RouteController.class);
		add("/tagManager", TagController.class);
		add("/orderManager", OrderController.class);
		add("/corps", CorpsController.class);
		add("/leader", LeaderController.class);
		add("/public", PublicController.class);
		add("/login", LoginController.class);
		add("/sys/menu", MenuController.class);
		add("/orderLog", OrderLogController.class);
		add("/operationLog", OperationLogController.class);
		add("/sys/data", DataDictionaryController.class);
		add("/account", AccountController.class);
		add("/report", ReportController.class);
	}

}
