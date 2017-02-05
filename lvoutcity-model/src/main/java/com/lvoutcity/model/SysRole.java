package com.lvoutcity.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.model.base.BaseSysRole;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysRole extends BaseSysRole<SysRole> {
	public static final SysRole dao = new SysRole();
	/**
	 * 
	 * @Title: queryRoleList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午10:38:51
	 * @create_user James w.s
	 * @throws
	 */
	public List<SysRole> queryRoleList(){
		return dao.find(" select * from t_sys_role r where r.is_delete = 1 and r.status = 1 ");
	}
	
	public Page<Record> queryRoleClub(Integer page,Integer size,String type){
		SQuery sq = SQuery.create("select r.id,r.role_name,r.role_code,r.is_delete,r.belongs,r.create_time,r.create_user,r.remark,r.status ");
		sq.append(" from t_sys_role r ");
		sq.append(" where r.is_delete = 1 ");
		sq.append(" and r.status = 1 ");
		if("1".equals(type)) {
			sq.append(" and r.belongs = 0 "); 
		}else 
		{
			sq.append(" and r.belongs = 1 ");
		}
		return Db.paginate(page, size,	sq.isSql(), sq.isFrom());
	}
	
}