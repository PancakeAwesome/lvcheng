package com.lvoutcity.admin.controller;

import java.util.List;

import javax.servlet.http.Cookie;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.CaptchaRender;
import com.lvoutcity.admin.Interceptor.LoginInterceptor;
import com.lvoutcity.admin.utils.CaptchaRewriteRender;
import com.lvoutcity.admin.utils.PermissionBean;
import com.lvoutcity.admin.utils.WebUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.Guide;
import com.lvoutcity.model.TouristInfo;


public class IndexController extends BaseController {
	
	
	public void index() {
		try {
			//System.out.println("afa"+getSessionAttr("user_back"));
			
			/*if(WebUtils.LoginOut(getRequest())){
				render("login.html");
				return;
			}*/
			PermissionBean perm = WebUtils.getPermission(getRequest());
			String menu = generateMenuDiv(perm.getPermissionMenus());
			setAttr("menu", menu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		render("index.html");
	}

	@Clear(LoginInterceptor.class)
	public void login() {
		 Cookie idCookie = new Cookie(Constants.COOKIE_NAME, LvoutcityUtils.getUUID()); //可以使用md5或着自己的加密算法保存     
         idCookie.setPath("/");   
         getResponse().addCookie(idCookie);
		render("login.html");
	}

	/**
	 * @time 2013-9-11  下午09:10:24
	 * @param menus
	 * @return String
	 * @description generate top menu div
	 * @since 1.0
	 */
	private String generateMenuDiv(List<ZTree> menus){
		StringBuilder sb = new StringBuilder();
		String iconCls = null;
		sb.append("<div class=\"menu_dropdown bk_2\">");
		for(ZTree menu : menus){
			iconCls = LvoutcityUtils.isNotNull(menu.getAttributes().get(ZTree.ICON_CLS)) ? menu.getAttributes().get(ZTree.ICON_CLS).toString() : null;
			sb.append("<dl id=\"menu-system\">");
			sb.append("<dt><i class=\"Hui-iconfont\">"+iconCls+"</i> "+menu.getName()+"<i class=\"Hui-iconfont menu_dropdown-arrow\">&#xe6d5;</i></dt>");
			sb.append("<dd>");
			generateChildrenMenuDiv(menu.getChildren(), sb);
			sb.append("</dd>");
			sb.append("</dl>");
		}
		sb.append(" </div> ");
		return sb.toString();
	}

	/** @time 2013-9-11  下午09:10:03
	 * @param children
	 * @param sb
	 * @return void
	 * @description generate child menu div
	 * @since 1.0
	 */
	private void generateChildrenMenuDiv(List<ZTree> children, StringBuilder sb) {
		if(children.isEmpty()){
			return;
		}
		sb.append("<ul>");
		String iconCls = null;
		for(ZTree menu : children){
		//	System.out.println(menu.getAttributes().get(ZTree.URL));
			iconCls = LvoutcityUtils.isNotNull(menu.getAttributes().get(ZTree.ICON_CLS)) ? menu.getAttributes().get(ZTree.ICON_CLS).toString() : null;
			if(!menu.getChildren().isEmpty()){
				sb.append("<div style=\"padding-left:20px;\" class=\"easyui-accordion\"  fit=\"false\" border=\"false\"><div></div>");
				sb.append("<div title=\"" + menu.getName() + "\" iconCls=\"" + (LvoutcityUtils.isNull(iconCls) ? "folder" : iconCls) + "\">");
				generateChildrenMenuDiv(menu.getChildren(), sb);
				sb.append("</div></div>");
			}
			else{
				sb.append("<li>");
				sb.append("<a _href=\""+menu.getAttributes().get(ZTree.URL)+"\" data-title=\""+menu.getName()+"\" href=\"javascript:void(0)\">"+menu.getName()+"</a>");
				sb.append("</li>");
			}
		}
		sb.append("</ul>");
	}
	
	
	/**
	 * 
	 * @Title: patchca 
	 * @Description: 图形验证码
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:58:03
	 * @create_user James w.s
	 * @throws
	 */
	@Clear(LoginInterceptor.class)
	public void patchca() {
		try{
			CaptchaRewriteRender.setCaptchaName("lvvc");
			CaptchaRewriteRender img = new CaptchaRewriteRender();
			render(img);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Clear(LoginInterceptor.class)
	public void welcome() {
		// 获取当前用户
		String clubId=getSessionAttr("clubId");
		String id = null;
		String sql ="";
		if(!Constants.LVC_CLUB_ID.equals(clubId)){
			sql += "and t.club_id ='"+clubId+"' ";
			id = clubId;
		}
		
		//统计攻略
		Record rc = Db.findFirst("select count(1) as total from t_guide t where t.is_delete='1' "+sql);
		setAttr("guide_total", rc.getLong("total"));
		Record rc1 = Db.findFirst("select count(1) as total from t_guide t where t.is_delete='1' and t.enabled='1' "+sql);
		setAttr("guide_up", rc1.getLong("total"));
		Record rc2 = Db.findFirst("select count(1) as total from t_route_mag t where t.is_delete='1' "+sql);
		setAttr("route_total", rc2.getLong("total"));
		Record rc3 = Db.findFirst("select count(1) as total from t_route_mag t where t.is_delete='1' and t.status='2' "+sql);
		setAttr("route_up", rc3.getLong("total"));
		
		//Record rc4 = Db.findFirst("select count(1) as total from t_user t  ");
		setAttr("user_total", TouristInfo.dao.countTouristsByClub(id));
		
		Record rc5 = Db.findFirst("select count(1) as total from t_order_record t1 where 1=1 "+CommonSql.ORDER_STATUS);
		setAttr("order_total", rc5.getLong("total"));
		
		System.out.println("");
	}
}
