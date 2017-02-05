package com.lvoutcity.admin.context;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.servlet.ShiroFilter;

import com.jfinal.core.JFinalFilter;

/** 
* @ClassName: ContextLoaderListener 
* @Description: 
* @author Gdh
* @date 2016年4月25日 下午12:52:29 
*  
*/
@WebListener
public class ContextLoaderListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		EnvironmentLoader environmentLoader = new EnvironmentLoader();
		environmentLoader.initEnvironment(event.getServletContext());
		
		javax.servlet.FilterRegistration.Dynamic shiroFilter = event.getServletContext().addFilter(ShiroFilter.class.getSimpleName(), ShiroFilter.class);
		shiroFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true, "/*");
		
		javax.servlet.FilterRegistration.Dynamic jfinalFilter = event.getServletContext().addFilter(JFinalFilter.class.getSimpleName(), JFinalFilter.class);
		jfinalFilter.setInitParameter("configClass", AdminConfig.class.getName());
		jfinalFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true, "/*");
		
	}

}
