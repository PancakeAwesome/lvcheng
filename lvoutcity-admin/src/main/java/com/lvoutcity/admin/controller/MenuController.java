package com.lvoutcity.admin.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.admin.utils.WebUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.SysMenu;

/**
 * 
 * @ClassName: MenuController 
 * @Description: 菜单管理
 * @author James w.s
 * @date 2016年5月18日 下午2:44:13 
 *
 */

public class MenuController extends BaseController {

	/**
	 * 
	 * @Title: index 
	 * @Description: 菜单显示
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:46:42
	 * @create_user James w.s
	 * @throws
	 */
	public void index(){
		render("index.html");
	}
	
	/**
	 * 
	 * @Title: menu 
	 * @Description: 菜单列表页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午6:59:38
	 * @create_user James w.s
	 * @throws
	 */
	public void menu(){
		setAttr("superId", getPara("superId"));
		render("menu.html");
	}
	
	
	/**
	 * 
	 * @Title: getMenuList 
	 * @Description: 获取菜单列表
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:48:15
	 * @create_user James w.s
	 * @throws
	 */
	public void list(){
		SQuery sq = SQuery.create();
		sq.append(" select * from t_sys_menu m where is_delete = '1' and super_id ='0' ");
		List<SysMenu> list = SysMenu.dao.find(sq.getSQuery());
		
		List<ZTree> nodeList = new ArrayList<ZTree>();
		for(SysMenu ar : list){
			ZTree node = new ZTree(ar.getId(),ar.getSuperId(),ar.getName(),ar.getMenuUrl());
			nodeList.add(node);
		}
		renderJson(list);
	}
	
	/**
	 * 
	 * @Title: menuList 
	 * @Description: 菜单列表
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午4:56:35
	 * @create_user James w.s
	 * @throws
	 */
	public void menuList(){
		try {
			String superId = getPara("superId");
			SQuery sql = SQuery.create("select m.id as id ,m.name as name,m.menu_url as menuUrl,date_format(m.create_time,'%Y-%m-%d %T') as createTime, ");
			sql.append(" bk.real_name as createUser ");
			sql.append("from t_sys_menu m left join t_user bk on m.create_user = bk.id ");
			sql.append(" where m.is_delete='1' and m.role_menu is null ");
			if(LvoutcityUtils.isNotNull(superId)){
				sql.append(" and m.super_id ='"+superId+"'");
			}else{
				sql.append(" and m.super_id != 0");
			}
			renderDatatable(LvoutcityUtils.getPage(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
					sql.isSql(), sql.isFrom())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title: add 
	 * @Description: 菜单添加页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:48:43
	 * @create_user James w.s
	 * @throws
	 */
	public void add(){
		String superId = getPara("superId");
		if(LvoutcityUtils.isNotNull(superId)){
			SysMenu superMenu = SysMenu.dao.findById(superId);
			if(LvoutcityUtils.isNotNull(superMenu)){
				superMenu.setSuperId(superId);
				superMenu.setSuperName(superMenu.getName());
				setAttr("bean", superMenu);
				render("add.html");
				return;
			}
		}
		setAttr("bean",new SysMenu());
		render("add.html");
	}
	
	/**
	 * 
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:49:04
	 * @create_user James w.s
	 * @throws
	 */
	public void edit(){
		String id = getPara("id");
		if(LvoutcityUtils.isNotNull(id)){
			SysMenu menu = SysMenu.dao.findById(id);
			if(LvoutcityUtils.isNotNull(menu)){
				SysMenu superMenu = SysMenu.dao.findById(menu.getId());
				if(LvoutcityUtils.isNotNull(superMenu)){
					menu.setSuperId(superMenu.getName());
					menu.setSuperName(superMenu.getName());
				}
				setAttr("bean", menu);
			}
		}
		render("add.html");
	}
	
	/**
	 * 
	 * @Title: saveOrUpdate 
	 * @Description: 添加或修改菜单
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:49:52
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdate(){
		SysMenu bean = getBean(SysMenu.class);
		if(LvoutcityUtils.isNotNull(bean.getId())){
			SysMenu newBean = SysMenu.dao.findById(bean.getId());
			if(LvoutcityUtils.isNotNull(bean.getName())){
				newBean.set("name", bean.getName());
			}
			if(LvoutcityUtils.isNotNull(bean.getSuperId())){
				newBean.set("super_id", bean.getSuperId());
			}
			if(LvoutcityUtils.isNotNull(bean.getMenuUrl())){
				newBean.set("menu_url", bean.getMenuUrl());
			}
			if(LvoutcityUtils.isNotNull(bean.getRoleMenu())){
				newBean.set("role_menu", bean.getRoleMenu());
			}
			if(LvoutcityUtils.isNotNull(bean.getEnabled())){
				newBean.set("enabled", bean.getEnabled());
			}
			if(LvoutcityUtils.isNotNull(bean.getIconCls())){
				newBean.set("icon_cls", bean.getIconCls());
			}
			if(LvoutcityUtils.isNotNull(bean.getOrderBy())){
				newBean.set("order_by", bean.getOrderBy());
			}
			if(LvoutcityUtils.isNotNull(bean.getOrderType())){
				newBean.set("order_type", bean.getOrderType());
			}
			newBean.setUpdateTime(new Date());
			newBean.setUpdateUser(getSessionAttr("current_user_id"));
			newBean.update();
			setAttr("bean", newBean);
		}else{
			bean.setId(LvoutcityUtils.getUUID());
			if(LvoutcityUtils.isNull(bean.getSuperId())){
				bean.setSuperId("0");
			}
			bean.setCreateTime(new Date());
			bean.setCreateUser(getSessionAttr("current_user_id"));
			bean.setIsDelete(Constants.ISDELETE_FALSE);
			bean.save();
			setAttr("bean",bean);
		}
		renderJson("success","操作成功");
	}
	
	/**
	 * 
	 * @Title: delet 
	 * @Description: 删除菜单
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:50:19
	 * @create_user James w.s
	 * @throws
	 */
	public void delete(){
		String id = getPara("id");
		SysMenu menu = SysMenu.dao.findById(id);
		if(LvoutcityUtils.isNotNull(menu)){
			menu.deleteById(menu.getId());
		}
		renderJson("success","操作成功");
	}
	
	/**
	 * 
	 * @Title: indexMenuBtn 
	 * @Description: 菜单按钮列表
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:51:59
	 * @create_user James w.s
	 * @throws
	 */
	public void indexMenuBtn(){
		try {
			String superId = getPara("superId");
			setAttr("superId", superId);
			render("menu_btn_list.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @Title: btnList 
	 * @Description: 按钮list 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:40:49
	 * @create_user James w.s
	 * @throws
	 */
	public void btnList(){
		String superId = getPara("superId");
		SQuery find = SQuery.create("");
		find.append(" select id as id,name as name ,menu_url as menuUrl ,date_format(create_time,'%Y-%m-%d %T') as createTime ");
		find.append(" ,order_by as orderBy from t_sys_menu m where m.is_delete='1' and m.super_id='"+superId+"' ");
		find.append("  order by m.create_time desc ");
		renderDatatable(LvoutcityUtils.getPage(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				find.isSql(), find.isFrom())));
	}
	
	/**
	 * 
	 * @Title: addBtnMenu 
	 * @Description: 菜单按钮
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:40:10
	 * @create_user James w.s
	 * @throws
	 */
	public void addBtnMenu(){
		String superId = getPara("superId");
		SysMenu menu = new SysMenu();
		if(LvoutcityUtils.isNotNull(superId)){
			SysMenu superMenu = SysMenu.dao.findById(superId);
			if(LvoutcityUtils.isNotNull(superMenu)){
				menu.setSuperId(superId);
				menu.setSuperName(superMenu.getName());
				menu.setOrderType(superMenu.getOrderType());
				setAttr("bean", menu);
				render("menu_btn_add.html");
				return;
			}
		}
		setAttr("bean",menu);
		render("menu_btn_add.html");
	}
	
	/**
	 * 
	 * @Title: editBitMenu 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午3:49:31
	 * @create_user James w.s
	 * @throws
	 */
	public void editBitMenu(){
		String id = getPara("id");
		if(LvoutcityUtils.isNotNull(id)){
			SysMenu menu = SysMenu.dao.findById(id);
			if(LvoutcityUtils.isNotNull(menu) && !"0".equals(menu.getSuperId())){
				SysMenu superMenu = SysMenu.dao.findById(menu.getSuperId());
				if(LvoutcityUtils.isNotNull(superMenu)){
					menu.setOrderType(superMenu.getOrderType());
					menu.setSuperId(superMenu.getId());
					menu.setSuperName(superMenu.getName());
				}
				setAttr("bean", menu);
			}
		}
		render("menu_btn_add.html");
		
	}
	
	/**
	 * 
	 * @Title: saveOrUpdateMenu 
	 * @Description: 菜单按钮添加
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:50:47
	 * @create_user James w.s
	 * @throws
	 */
	public void saveOrUpdateMenu(){
		String superId = getPara("superId");
		if(LvoutcityUtils.isNotNull(superId)){
			
		}
	}
	
	/**
	 * 
	 * @Title: addMenuBtn 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:54:24
	 * @create_user James w.s
	 * @throws
	 */
	public void addMenuBtn(){
		String superId = getPara("superId");
		if(LvoutcityUtils.isNotNull(superId)){
			SysMenu superMenu = SysMenu.dao.findById(superId);
			if(LvoutcityUtils.isNotNull(superMenu)){
				superMenu.setSuperId(superId);
				superMenu.setSuperName(superMenu.getName());
				setAttr("bean", superMenu);
				render("menu_add.html");
				return;
			}
		}
		setAttr("bean",new SysMenu());
		render("menu_add.html");
	}
	
	/**
	 * 
	 * @Title: editMenuBit 
	 * @Description: 修改按钮
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午3:00:01
	 * @create_user James w.s
	 * @throws
	 */
	public void editMenuBit(){
		String id = getPara("id");
		if(LvoutcityUtils.isNotNull(id)){
			SysMenu menu = SysMenu.dao.findById(id);
			if(LvoutcityUtils.isNotNull(menu) && !"0".equals(menu.getSuperId())){
				SysMenu superMenu = SysMenu.dao.findById(menu.getSuperId());
				if(LvoutcityUtils.isNotNull(superMenu)){
					menu.setSuperId(superMenu.getId());
					menu.setSuperName(superMenu.getName());
				}
				setAttr("bean", menu);
			}
		}
		render("menu_add.html");
		
	}
	
	/**
	 * 
	 * @Title: deleteMenuBtn 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:55:00
	 * @create_user James w.s
	 * @throws
	 */
	public void deleteMenuBtn(){
		
	}
	
}
