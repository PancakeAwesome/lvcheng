package com.lvoutcity.admin.context;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisInterceptor;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.lvoutcity.admin.Interceptor.ExceptionInterceptor;
import com.lvoutcity.admin.Interceptor.LoginInterceptor;
import com.lvoutcity.admin.Interceptor.SessionInterceptor;
import com.lvoutcity.admin.handler.ContextPathHandler;
import com.lvoutcity.admin.handler.SessionHandler;
import com.lvoutcity.core.redis.DbRedisCache;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model._MappingKit;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/** 
* @ClassName: AdminConfig 
* @Description: 
* @author Gdh
* @date 2016年4月25日 下午12:52:49 
*  
*/
public class AdminConfig extends JFinalConfig{

	private Routes routes = null;
	
	@Override
	public void configConstant(Constants me) {
		

		loadPropertyFile("config.props");
		
		me.setDevMode(true);
		me.setViewType(ViewType.FREE_MARKER);
		me.setEncoding("utf-8");
		me.setJsonFactory(FastJsonFactory.me());
		me.setBaseViewPath("/WEB-INF/view");
		
//		try {
//			String url="http://localhost:8080";
//			  if(!LvoutcityUtils.isNull(getProperty("http.url"))){
//				  url = getProperty("http.url");
//			  }
//			//Configuration configuration = FreeMarkerRender.getConfiguration();
//			//configuration.setSharedVariable("ctxPath", JFinal.me().getContextPath());
//			//configuration.setSharedVariable("basePath", url+JFinal.me().getContextPath()+"/");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void configRoute(Routes me) {
		me.add(new AdminRoutes());
		this.routes = me;
	}

	@Override
	public void configPlugin(Plugins me) {
		
		RedisPlugin redisPlugin = new RedisPlugin(JFinal.me().getContextPath(), getProperty("redis.host"),getPropertyToInt("redis.port"),getProperty("redis.password"));
		me.add(redisPlugin);
		
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbc.url"), getProperty("jdbc.user"),getProperty("jdbc.password"),getProperty("jdbc.driverClassName"));
		me.add(druidPlugin);
		
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setShowSql(true);
		activeRecordPlugin.setCache(new DbRedisCache());
		_MappingKit.mapping(activeRecordPlugin);
		me.add(activeRecordPlugin);
		
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
		shiroPlugin.setUnauthorizedUrl("/403.html");
		shiroPlugin.setLoginUrl("/login.html");
		me.add(shiroPlugin);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
	
		me.add(new RedisInterceptor());
		me.add(new ShiroInterceptor());
		me.add(new TxByActionKeyRegex(".*"));
		me.add(new SessionInterceptor());
		me.add(new LoginInterceptor());	
		me.add(new ExceptionInterceptor());
		
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new UrlSkipHandler("main.html", true));
		me.add(new ContextPathHandler());
		me.add(new SessionHandler());
//		String path = request.getContextPath();
//        String basePath = request.getScheme() + "://"
//        + request.getServerName() + ":" + request.getServerPort()
//        + path + "/";
//        request.setAttribute("basePath", basePath);
	}

}
