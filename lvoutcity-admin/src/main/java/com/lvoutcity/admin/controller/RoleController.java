package com.lvoutcity.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.HuiUtils;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.RoleMenu;
import com.lvoutcity.model.SysMenu;
import com.lvoutcity.model.SysRole;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.UserRole;

import net.sf.json.JSONArray;

/**
 * 
 * @ClassName: RoleController 
 * @Description: 角色管理
 * @author James w.s
 * @date 2016年5月31日 上午11:32:15 
 *
 */

public class RoleController extends BaseController {

	/**
	 * 默认方法
	 */
	public void index() {
		render("indexRole.html");
}

	/**
	 * 查询角色信息
	 */
	
	public void selectRole(){
		SysRole s = getBean(SysRole.class);
		SQuery sq = SQuery.create("select r.id,r.role_name,r.role_code,r.is_delete,r.belongs,r.create_time,r.create_user,r.remark,r.status ");
		sq.append(" from t_sys_role r ");
		sq.append(" where r.is_delete = 1 ");
		if(LvoutcityUtils.isNotNull(s.getRoleCode())){
			sq.append(" and r.role_code like '%"+s.getRoleCode()+"%'");
		}
		if(LvoutcityUtils.isNotNull(s.getRoleName())){
			sq.append(" and r.role_name  like '%"+s.getRoleName()+"%'");
		}
		sq.append(" order by  r.belongs asc,r.role_name desc, r.create_time asc ");
		renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				sq.isSql(), sq.isFrom()));
	}



	/**
	 * 查看角色详细信息
	 */
	
	public void roleShow(){
		SysRole sysRole = SysRole.dao.findById((getPara("id")));
		String view = getPara("view");
		if("1".equals(view)){
			// 设置页面类型 1 是新增 2是查看  3 是编辑
			setAttr("type",2);
		}else{
			setAttr("type",3);
		}
		
		setAttr("sysRole",sysRole);
		render("roleShow.html");
	}


	/**
	 * 角色更新
	 */
	@Before(POST.class)
	public void saveOrUpdate(){
		try {
			SysRole sysRole = getBean(SysRole.class);
			if(LvoutcityUtils.isNotNull(sysRole.getId())){
				SysRole newRole = SysRole.dao.findById(sysRole.getId());
				if(LvoutcityUtils.isNotNull(newRole)){
					if(LvoutcityUtils.isNotNull(sysRole.getRoleName())){
						newRole.set("role_name", sysRole.getRoleName());
					}
					if(LvoutcityUtils.isNotNull(sysRole.getBelongs())){
						newRole.set("belongs", sysRole.getBelongs());
					}
					if(LvoutcityUtils.isNotNull(sysRole.getStatus())){
						newRole.set("status", sysRole.getStatus());
					}
					if(LvoutcityUtils.isNotNull(sysRole.getRemark())){
						newRole.set("remark", sysRole.getRemark());
					}
					newRole.update();
				}
				renderJson("success", "修改成功!");
			}else{
				sysRole.setId(LvoutcityUtils.getUUID());
				sysRole.setCreateTime(new Date());
				sysRole.setCreateUser("");
				sysRole.setIsDelete(Constants.ISDELETE_FALSE);
				sysRole.save();

				renderJson("success", "保存成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJson("error", "操作异常!");
		}
	}
	
	
	/**
	 * 用户删除
	 */
	public void delUser() {
		String id = "";
		Map<String,String[]> idMap = getParaMap();
		String[] idList = idMap.get("id[]");
		for (int i = 0; i < idList.length; i++) {
			id = idList[i];
			List<UserRole> user = UserRole.dao.find("select * from t_user_role u  where u.role_id ='" + id + "'");
			if (!user.isEmpty()) {
				renderJson("error", "无法删除!关联用户信息!");
				return;
			} else {
				SysRole bean = SysRole.dao.findById(id);
				bean.deleteById(bean.getId());
				renderJson("result", "删除成功!");
			}
		}
	}

	/**
	 * 角色新增页面
	 */
	
	public void roleAdd(){
		List<TourismClub> list = TourismClub.dao.find("select * from t_tourism_club where enabled ='1' and is_delete='0' ");
		List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
		for(TourismClub t : list){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", t.getId());
			m.put("name", t.getClubName());
			map.add(m);
		}
		net.sf.json.JSONArray ss = net.sf.json.JSONArray.fromObject(map);
		setAttr("list",ss);
		
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("sysRole", new SysRole());
		setAttr("type",1);
		render("roleShow.html");
	}
	
	/**
	 * 编辑角色信息
	 */
	
	public void roleEdit(){
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		SysRole sysRole = SysRole.dao.findById((getPara("id")));
		setAttr("sysRole", sysRole);
		setAttr("type",getPara("type"));
		render("roleShow.html");
	}

	/**
	 * 
	 * @Title: getRolePermissionJsp 
	 * @Description: 权限页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午11:10:42
	 * @create_user James w.s
	 * @throws
	 */
	public void getRolePermissionJsp(){
		setAttr("roleId", getPara("roleId") );
		render("role_menu.html");
	}
	
	/**
	 * 
	 * @Title: getRoleCompetence 
	 * @Description: 菜单列表
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午11:07:57
	 * @create_user James w.s
	 * @throws
	 */
	public void getRoleCompetence(){
		String roleId = getPara("roleId");
		List<SysMenu> list = SysMenu.dao.find("select * from t_sys_menu m where m.is_delete =1 and m.name != -1 order by m.order_by asc ");
		List<ZTree> zTree = new ArrayList<ZTree>();
		for(SysMenu sys : list){
			ZTree z = new ZTree(sys.getId(),sys.getName(),sys.getSuperId(),"");
			zTree.add(z);
		}
		List<ZTree> rolezTree = new ArrayList<ZTree>();
		List<SysMenu> role = SysMenu.dao.find("select m.id from t_sys_menu m inner join t_role_menu r on r.menu_id = m.id where m.is_delete =1 and m.name != -1  and r.role_id = '"+roleId+"' order by m.order_by asc ");
		for(SysMenu sys : role){
			ZTree z = new ZTree(sys.getId(),sys.getName(),sys.getSuperId(),"");
			rolezTree.add(z);
		}
		renderJson(HuiUtils.toTreeList(zTree, rolezTree,"0"));
	}
	
	/**
	 * 
	 * @Title: getRoleCompetenceRole 
	 * @Description: 获取已经选择的菜单
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午11:43:14
	 * @create_user James w.s
	 * @throws
	 */
	public void getRoleCompetenceRole(){
		String roleId = getPara("roleId");
		List<SysMenu> role = SysMenu.dao.find("select m.id from t_sys_menu m inner join t_role_menu r on r.menu_id = m.id where m.is_delete =1 and r.role_id = '"+roleId+"' ");
		renderJson(role);
	}
	
	/**
	 * 
	 * @Title: saveRoleMenu 
	 * @Description: 添加角色权限
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午3:07:16
	 * @create_user James w.s
	 * @throws
	 */
	public void saveRoleMenu(){
		String roleId = getPara("roleId");
		Db.update("delete from t_role_menu where role_id ='"+roleId+"'");
		String menuArray = getPara("menuArray");
		String menus[] = menuArray.split(",");
		List<RoleMenu> list = new ArrayList<RoleMenu>();
		for(String str : menus){
			RoleMenu r = new RoleMenu();
			r.setId(LvoutcityUtils.getUUID());
			r.setMenuId(str);
			r.setRoleId(roleId);
			list.add(r);
		}
		Db.batchSave(list, list.size());
	}

	/**
	 * 
	 * @Title: getRoleUserJsp 
	 * @Description: 获取选取角色用户
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午10:34:08
	 * @create_user James w.s
	 * @throws
	 */
	public void getRoleUserJsp(){
		setAttr("userId", getPara("userId"));
		String cublId = getPara("cublId");
		if(LvoutcityUtils.isNull(cublId)){
			 renderJson("error","请选择俱乐部!");
			return;
		}
		setAttr("type",cublId.equals(Constants.LVC_CLUB_ID) ? "1" : "0" );
		List<UserRole> userRoleList = UserRole.dao.queryUserRoleList(getPara("userId"));
		setAttr("rList", userRoleList);
		render("user_role.html");
	}
	
	/**
	 * 
	 * @Title: getRoleUserList 
	 * @Description: 查询权限
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午11:50:42
	 * @create_user James w.s
	 * @throws
	 */
	public void getRoleUserList(){
		renderDatatableFromDb(SysRole.dao.queryRoleClub((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),getPara("type")));
	}
	
	/**
	 * 
	 * @Title: getRoleUserCheck 
	 * @Description: 已经选择的用户权限
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午3:21:09
	 * @create_user James w.s
	 * @throws
	 */
	public void getRoleUserCheck(){
		String userId = getPara("userId");
		List<UserRole> userRoleList = UserRole.dao.queryUserRoleList(userId);
		renderJson(JSONArray.fromObject(userRoleList));
	}
	
	
	
	/**
	 * 
	 * @Title: saveUserRole 
	 * @Description: 保存用户添加的数据
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午10:53:09
	 * @create_user James w.s
	 * @throws
	 */
	public void saveUserRole(){
		String roleArray = getPara("roleArray");
		String userId = getPara("userId");
		String roles [] =roleArray.split(",");
		List<UserRole> list = new ArrayList<UserRole>();
		for(String str : roles){
			UserRole bean = new UserRole();
			bean.setId(LvoutcityUtils.getUUID());
			bean.setRoleId(str);
			bean.setUserId(userId);
			list.add(bean);
		}
		Db.batchSave(list, list.size());
	}
}
