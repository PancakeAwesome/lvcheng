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
public class HtmlHandler extends Handler {

	   public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {  
	        int index = target.lastIndexOf("index.html");  
	        if (index != -1){
	        	return;
	        }else{
	        next.handle(target, request, response, isHandled); 
	        }
	    }  

}
