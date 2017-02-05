/**
 * 
 */
package com.lvoutcity.admin.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * @author wj
 *
 */
public class ContextPathHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		// TODO Auto-generated method stub
	  String path = request.getContextPath();
	  //System.out.println("path"+path);
      String basePath = request.getScheme() + "://"
      + request.getServerName() + ":" + request.getServerPort()
      //+"/";
      + path + "/";
      request.setAttribute("basePath", basePath);
      request.setAttribute("ctxPath", path + "/");
      
      next.handle(target, request, response, isHandled);
	}

}
