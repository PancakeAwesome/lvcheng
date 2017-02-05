/**
 * 
 */
package com.lvoutcity.admin.controller;

import java.io.File;
import java.util.List;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.ExportExcel;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;

/**
 * @author wj
 *
 */
public class OperationLogController extends BaseController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 首页跳转
	 * @throws Exception
	 */
	public void  index() throws Exception{
		
	}

	/**
	 * 查询交易日志
	 */
	public void selectOptLogList()throws Exception{
		String sql = " FROM t_user_opt_log tl LEFT JOIN t_user tu ON tl.user_id = tu.id LEFT JOIN t_user_backgroup tb ON tl.user_id = tb.user_id "
				+ " where 1=1 ";
		// 查询条件添加
		// 如果不是超级管理员，添加俱乐部限制
		if(getSessionAttr("user")!=null){
			User user = getSessionAttr("user");
			if(user.getUserNo() == Constants.ADMIN_NO){
				// 不做处理
			}else{
				//添加俱乐部过滤条件
				UserBackgroup ub = getSessionAttr("user_back");
				sql += " and tb.club_id ='"+ub.getClubId()+"' ";
				
			}
		}else{
		 // 跳转登录页面
		}
		// 旅城号
		if(getPara("userNo")!=null&& !getPara("userNo").trim().isEmpty()){
			sql += " and tu.user_no ='"+getPara("userNo").trim()+"' ";
		}
		//用户名
		if(getPara("userName")!=null&& !getPara("userName").trim().isEmpty()){
			sql += " and tb.user_name ='"+getPara("userName").trim()+"' ";
		}
		// 开始时间
		if(getPara("start_time")!=null&& !getPara("start_time").trim().isEmpty()){
			sql += " and unix_timestamp(tl.create_time) >= unix_timestamp('"+getPara("start_time").trim()+"') ";
		}
		// 结束时间
		if(getPara("end_time")!=null&& !getPara("end_time").trim().isEmpty()){
			sql += " and unix_timestamp(tl.create_time) <= unix_timestamp('"+getPara("end_time").trim()+"') ";
		}
		
		Page pg = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), CommonSql.OPERATION_LOG_SELECT, sql+" order by tl.create_time desc ");
		renderDatatableFromDb(pg);
	}
	
	public void export() throws Exception {

		String sql = " FROM t_user_opt_log tl LEFT JOIN t_user tu ON tl.user_id = tu.id LEFT JOIN t_user_backgroup tb ON tl.user_id = tb.user_id "
				+ " where 1=1 ";
		// 查询条件添加
		// 如果不是超级管理员，添加俱乐部限制
		if(getSessionAttr("user")!=null){
			User user = getSessionAttr("user");
			if(user.getUserNo() == Constants.ADMIN_NO){
				// 不做处理
			}else{
				//添加俱乐部过滤条件
				UserBackgroup ub = getSessionAttr("user_back");
				sql += " and tb.club_id ='"+ub.getClubId()+"' ";
				
			}
		}else{
		 // 跳转登录页面
		}
		// 旅城号
		if(getPara("userNo")!=null&& !getPara("userNo").trim().isEmpty()){
			sql += " and tu.user_no ='"+getPara("userNo").trim()+"' ";
		}
		//用户名
		if(getPara("userName")!=null&& !getPara("userName").trim().isEmpty()){
			sql += " and tb.user_name ='"+getPara("userName").trim()+"' ";
		}
		// 开始时间
		if(getPara("start_time")!=null&& !getPara("start_time").trim().isEmpty()){
			sql += " and unix_timestamp(tl.create_time) >= unix_timestamp('"+getPara("start_time").trim()+"') ";
		}
		// 结束时间
		if(getPara("end_time")!=null&& !getPara("end_time").trim().isEmpty()){
			sql += " and unix_timestamp(tl.create_time) <= unix_timestamp('"+getPara("end_time").trim()+"') ";
		}
		
		Record rc = Db.findFirst(" select count(1) as total "+sql);
		// 计算需要导出的总数
		long num = rc.getLong("total");
		String fileName = "../webapps/"+getRequest().getContextPath()+"/temp/"+"操作日志"+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";
		//fileName = "E:"+File.separator+"操作日志"+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";		
		ExportExcel et = new ExportExcel();
		et.createWork(fileName);
		String[] header = { "旅城号", "用户名", "操作详情", "状态", "操作时间", "错误原因" };
		String[] key = { "user_no", "user_name","action", "result", "create_time", "remark"};
		try{			
			// 遍历total
			
			for(long i =0;i<num/Constants.ROW_NUMBER+1;i++){
				String limit = "limit "+i*Constants.ROW_NUMBER+","+Constants.ROW_NUMBER+" ";
				List<Record>  lt  = Db.find(CommonSql.OPERATION_LOG_SELECT_EXPORT+sql+limit);
				et.exportExcel(fileName,"xx", header,key, lt, i*Constants.ROW_NUMBER+1, i);
			}
				
		}catch(Exception e){
			e.getMessage();
		}
		renderFile(new File(fileName));
	
	}
}
