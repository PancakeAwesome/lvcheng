/**
 * 
 */
package com.lvoutcity.admin.Interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.JsonData;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;
import com.lvoutcity.model.UserOptLog;

/**
 * @author wj
 *
 */
public class ExceptionInterceptor implements Interceptor {
	 //private static final Logger log = Logger.getLogger(ExceptionInterceptor.class);
	 private static final Log log = LogFactory.getLog(ExceptionInterceptor.class);
	@Override
	public void intercept(Invocation inv) {
		 BaseController controller = (BaseController)inv.getController();
		  HttpServletRequest request = controller.getRequest();
		  // 操作是否成功，默认是1
		   String isOk ="1";
		   String mark = "";
		  boolean successed = false;
		  try {
			  inv.invoke();
		   successed = true;
		  } catch (Exception e) {
			  isOk ="0";
			  //System.out.println("==========>"+System.getProperty("catalina.base"));
			  System.out.println(e);
			  log.error(e);
		   //
		   //doLog(inv,e);
		   //判断是否ajax请求
		         String header = request.getHeader("X-Requested-With");
		            boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
		            String msg = formatException(e);
		            mark = msg;
		            log.error(msg);
		            if(isAjax){
//		             msg = new StringBuilder().append("\"")
//		            .append(msg).append("\"").toString();
		             JsonData js = new JsonData();
		             js.setCode("999");
		             js.setMsg(msg);
		             controller.renderJson(js);
		            }else{
		            	// 跳转到失败页面
		             String redirctUrl = request.getHeader("referer");
		             if(StringUtils.isBlank(redirctUrl)){
		              redirctUrl = request.getRequestURI();
		             }
		             controller.setAttr("message", msg);
		             controller.setAttr("redirctUrl",redirctUrl);
		             controller.render("../public/failed.html");
		            }
		  }finally{
			  // 抓取日志
			  if(inv.getMethod().getAnnotation(LogDescription.class)!=null&&
					  !inv.getMethod().getAnnotation(LogDescription.class).value().trim().isEmpty()){
				 String value= inv.getMethod().getAnnotation(LogDescription.class).value();
				  // 插入操作日志
				 User user =  (User) request.getSession().getAttribute("user");
				 UserBackgroup ub = (UserBackgroup) request.getSession().getAttribute("user_back");
				 
				 new UserOptLog().set("id", LvoutcityUtils.getUUID())
				 .set("user_id", user.getId())
				 .set("action", value)
				 .set("result", isOk)
				 .set("create_time", new Date())
				 .set("remark",mark).save();
			}
			  
		  }
		
	}

	 private static String formatException(Exception e){
		   String message = null;
		         Throwable ourCause = e;
		         while ((ourCause = e.getCause()) != null) {
		             e = (Exception) ourCause;
		         }
		         String eClassName = e.getClass().getName().toLowerCase();
		         //一些常见异常提示
		         if("java.lang.NumberFormatException".toLowerCase().equals(eClassName)){
		             message = "请输入正确的数字";
		         }else if("java.lang.NullPointerException".toLowerCase().equals(eClassName)){
		             message = "空指针引用异常";
		         }else if("java.lang.ClassCastException".toLowerCase().equals(eClassName)){
		             message = "类型强制转换异常";
		         }else if("java.lang.IllegalArgumentException".toLowerCase().equals(eClassName)){
		             message = "传递非法参数异常";
		         }else if("java.lang.IndexOutOfBoundsException".toLowerCase().equals(eClassName)){
		             message = "下标越界异常";
		         }else if("java.lang.UnsupportedOperationException".toLowerCase().equals(eClassName)){
		        	 message = "不支持的操作异常";
		         }else if("com.mysql.jdbc.mysqldatatruncation".toLowerCase().equals(eClassName)){
		             message = "数据长度过长或数据格式错误";
		         }else {
		        	 message = "操作异常！";
		         }
		          

		         //替换特殊字符
		         message = message.replaceAll("\"", "'");
		         return message;
		 }
}
