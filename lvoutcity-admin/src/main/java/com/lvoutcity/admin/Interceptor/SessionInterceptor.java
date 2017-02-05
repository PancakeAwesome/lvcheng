package com.lvoutcity.admin.Interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.FreeMarkerRender;

import freemarker.ext.servlet.HttpSessionHashModel;

public class SessionInterceptor implements Interceptor {
    
@Override
public void intercept(Invocation inv) {
	// TODO Auto-generated method stub
	   //向freemarker中传jsp的内置对象
       Controller c = inv.getController();
       c.setAttr("request", c.getRequest());
       c.setAttr("response", c.getResponse());
       c.setAttr("session", new HttpSessionHashModel(c.getSession(), FreeMarkerRender.getConfiguration().getObjectWrapper()));
       inv.invoke();
}

}
