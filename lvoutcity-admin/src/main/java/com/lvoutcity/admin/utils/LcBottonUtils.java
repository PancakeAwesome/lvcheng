package com.lvoutcity.admin.utils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.RouteMag;
import com.lvoutcity.model.SysMenu;
import com.lvoutcity.model.User;

/**
 * 
 * @ClassName: LcBottonUtils 
 * @Description: 旅城操作按钮生成类型
 * @author James w.s
 * @date 2016年6月27日 下午5:27:01 
 *
 */
public class LcBottonUtils {

	
	
	
	/**
	 * 
	 * @Title: getButtonOptions 
	 * @Description: 生成操作按钮
	 * @param @param orderType
	 * @param @param request
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016 2016年6月30日 下午2:51:06
	 * @create_user James w.s
	 * @throws
	 */
	public static String getButtonOptions(String orderType,HttpServletRequest request){
		PermissionBean perm = WebUtils.getPermission(request);
		Map<String,List<SysMenu>> permMenu = perm.getPermissionRoles();
		List<SysMenu> menus = permMenu.get(orderType);
		return getButtonOptionsStr(menus);
	}
	
	
	public static String getButtonOptionsStr(List<SysMenu> list){
		StringBuilder sb = new StringBuilder();
		if(LvoutcityUtils.isNotNull(list)){
			for(SysMenu sys : list){
				if(1 ==  sys.getRoleMenu()){
					sb.append("<a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-success radius\"><i class=\"Hui-iconfont\">&#xe600;</i>"+sys.getName()+"</a> ");
				}else if(2== sys.getRoleMenu()){
					String str = sys.getIconCls() == null ? "&#xe695;" : sys.getIconCls();
					sb.append(" <a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-secondary radius\"><i class=\"Hui-iconfont\">"+str+"</i>"+sys.getName()+"</a> ");
				}
				else if(3== sys.getRoleMenu()){
					String str = sys.getIconCls() == null ? "&#xe60c;" : sys.getIconCls();
					sb.append(" <a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-primary radius\"><i class=\"Hui-iconfont\">"+str+"</i>"+sys.getName()+"</a> ");
				}
				else if(4== sys.getRoleMenu()){
					String str = sys.getIconCls() == null ? "&#xe6e2;" : sys.getIconCls();
					sb.append(" <a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-danger radius\"><i class=\"Hui-iconfont\">"+str+"</i>"+sys.getName()+"</a> ");
				}else{
					if(sys.getRoleMenu() <= 10){
						
						String str = sys.getIconCls() == null ? "" : sys.getIconCls();
						sb.append(" <a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-default radius\"><i class=\"Hui-iconfont\">"+str+"</i>"+sys.getName()+"</a> ");
					}
				}
			}
		}else{
			sb.append("<p><em>无权进行操作!</em></p>");
		}
		return sb.toString();
	}
	
	public static boolean getButtonOptions(String orderType,HttpServletRequest request,User user){
		PermissionBean perm = WebUtils.getPermission(request);
		Map<String,List<SysMenu>> permMenu = perm.getPermissionRoles();
		List<SysMenu> menus = permMenu.get(orderType);
		StringBuilder sb = new StringBuilder();
		if(LvoutcityUtils.isNull(menus)){
			return false;
		}
		for(SysMenu sys : menus){
			if(1 ==  sys.getRoleMenu()){
				sb.append("<a data-title = \""+sys.getName()+"\" onclick=\""+sys.getMenuUrl()+"\" href=\"javascript:;\" class=\"btn btn-success radius\"><i class=\"Hui-iconfont\">&#xe600;</i>添加</a> ");
			}
		}
		return sb.length() > 0 ? true : false;
	}
	
	public static String getButtonOptions(String orderType, HttpServletRequest request, String status,String type,String id) {
		PermissionBean perm = WebUtils.getPermission(request);
		Map<String, List<SysMenu>> permMenu = perm.getPermissionRoles();
		List<SysMenu> menus = permMenu.get(orderType);
		StringBuilder sb = new StringBuilder();
		if (LvoutcityUtils.isNull(menus)) {
			return null;
		}
		for (SysMenu sys : menus) {
			if(type.equals("3") && sys.getRoleMenu() == 11){
				sb.append("<input class=\"btn btn-success radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//				sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-success radius ml-5\">""</a> ");
			}else if(type.equals("0") || type.equals("2")){ // 编辑 复制
				System.out.println(sys.getRoleMenu());
				if(sys.getRoleMenu() == 11 || sys.getRoleMenu() == 12){
					String str = sys.getRoleMenu() == 11 ? "btn btn-success radius ml-5" : "btn btn-primary radius ml-5";
					sb.append("<input class=\""+str+"\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//					sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-primary radius ml-5\">"+sys.getName()+"</a> ");
				}
			}else{
				switch (status) {
				case "1":
					if( sys.getRoleMenu() == 13 ){
						sb.append("<input class=\"btn btn-success radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-success radius ml-5\">"+sys.getName()+"</a> ");
					}
					if(sys.getRoleMenu() == 14){
						sb.append("<input class=\"btn btn-danger radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-danger radius ml-5\">"+sys.getName()+"</a> ");
					}
					if(!RouteMag.dao.hasOrder(id) && sys.getRoleMenu() == 17){//有订单就不能撤销
						sb.append("<input class=\"btn btn-warning radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-warning radius ml-5\">"+sys.getName()+"</a> ");
					}
					continue;
				case "2":
					if( sys.getRoleMenu() == 16 ){
						sb.append("<input class=\"btn btn-danger radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-danger radius ml-5\">"+sys.getName()+"</a> ");
					}
					if(!RouteMag.dao.hasOrder(id) && sys.getRoleMenu() == 17){//有订单就不能撤销
						sb.append("<input class=\"btn btn-warning radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-warning radius ml-5\">"+sys.getName()+"</a> ");
					}
					continue;
				case "3":
					if( sys.getRoleMenu() == 15 ){
						sb.append("<input class=\"btn btn-primary radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-primary radius ml-5\">"+sys.getName()+"</a> ");
					}
					if(!RouteMag.dao.hasOrder(id) && sys.getRoleMenu() == 17){//有订单就不能撤销
						sb.append("<input class=\"btn btn-warning radius ml-5\" type=\"button\" value=\""+sys.getName()+"\" onclick=\"" + sys.getMenuUrl()	+ "\">");
//						sb.append("<a data-title = \"" + sys.getName() + "\" onclick=\"" + sys.getMenuUrl()	+ "\" href=\"javascript:;\" class=\"btn btn-warning radius ml-5\">"+sys.getName()+"</a> ");
					}
					continue;
				}
			}
		}
		return sb.toString();
	}
}
