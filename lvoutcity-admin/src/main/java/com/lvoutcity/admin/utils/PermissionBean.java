package com.lvoutcity.admin.utils;

import java.util.List;
import java.util.Map;

import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.SysMenu;
/**
 * 
 * @ClassName: PermissionBean 
 * @Description: 权限管理
 * @author James w.s
 * @date 2016年6月22日 下午7:31:03 
 *
 */
public class PermissionBean implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -7049353562944038674L;

	private List<ZTree> permissionMenus; // 拥有的按钮

	private Map<String, List<SysMenu>> permissionRoles; // 拥有的权限


	private List<ZTree> permMenus;

	public List<ZTree> getPermissionMenus() {
		return permissionMenus;
	}

	public void setPermissionMenus(List<ZTree> permissionMenus) {
		this.permissionMenus = permissionMenus;
	}

	public Map<String, List<SysMenu>> getPermissionRoles() {
		return permissionRoles;
	}

	public void setPermissionRoles(Map<String, List<SysMenu>> permissionRoles) {
		this.permissionRoles = permissionRoles;
	}

	public List<ZTree> getPermMenus() {
		return permMenus;
	}

	public void setPermMenus(List<ZTree> permMenus) {
		this.permMenus = permMenus;
	}

}
