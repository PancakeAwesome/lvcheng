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
public class OrderLogController extends BaseController {

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
	public void selectOrderLogList()throws Exception{
		String sql = " FROM t_pay_log tpl  LEFT JOIN t_order_record tor    ON tpl.order_id = tor.id  "
				+ " LEFT JOIN t_user tu     ON tpl.create_user = tu.id where 1=1 ";
		// 查询条件添加
		// 如果不是超级管理员，添加俱乐部限制
		if(getSessionAttr("user_back")!=null){
			UserBackgroup ub = getSessionAttr("user_back");
			if(ub.getClubId().equals(Constants.LVC_CLUB_ID)){
				// 不做处理
			}else{
				//添加俱乐部过滤条件
				sql += " and tor.club_id ='"+ub.getClubId()+"' ";
				
			}
		}else{
		 // 跳转登录页面
		}
		// 交易号
		if(getPara("tradeNo")!=null&& !getPara("tradeNo").trim().isEmpty()){
			sql += " and tpl.tpp_trade_no ='"+getPara("tradeNo").trim()+"' ";
		}
		//订单号
		if(getPara("orderNo")!=null&& !getPara("orderNo").trim().isEmpty()){
			sql += " and tor.order_no ='"+getPara("orderNo").trim()+"' ";
		}
		// 手机
		if(getPara("phoneNumber")!=null&& !getPara("phoneNumber").trim().isEmpty()){
			sql += " and tu.phone_number ='"+getPara("phoneNumber").trim()+"' ";
		}
		// 开始时间
		if(getPara("start_time")!=null&& !getPara("start_time").trim().isEmpty()){
			sql += " and unix_timestamp(tpl.create_time) >= unix_timestamp('"+getPara("start_time").trim()+"') ";
		}
		// 结束时间
		if(getPara("end_time")!=null&& !getPara("end_time").trim().isEmpty()){
			sql += " and unix_timestamp(tpl.create_time) <= unix_timestamp('"+getPara("end_time").trim()+"') ";
		}
		
		
		Page pg = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), CommonSql.ORDER_LOG_SELECT, sql + " order by tor.order_no desc,tpl.create_time desc ");
		renderDatatableFromDb(pg);
	}
	
	/**
	 * 交易日志导出
	 * @throws Exception
	 */
	public void export()throws Exception{
		String sql = " FROM t_pay_log tpl  LEFT JOIN t_order_record tor    ON tpl.order_id = tor.id  "
				+ " LEFT JOIN t_user tu     ON tpl.create_user = tu.id where 1=1 ";
		// 查询条件添加
		// 如果不是超级管理员，添加俱乐部限制
		// 如果不是超级管理员，添加俱乐部限制
				if(getSessionAttr("user_back")!=null){
					UserBackgroup ub = getSessionAttr("user_back");
					if(ub.getClubId().equals(Constants.LVC_CLUB_ID)){
						// 不做处理
					}else{
						//添加俱乐部过滤条件
						sql += " and tor.club_id ='"+ub.getClubId()+"' ";
						
					}
				}else{
				 // 跳转登录页面
				}
		// 交易号
		if(getPara("tradeNo")!=null&& !getPara("tradeNo").trim().isEmpty()){
			sql += " and tpl.tpp_trade_no ='"+getPara("tradeNo").trim()+"' ";
		}
		//订单号
		if(getPara("orderNo")!=null&& !getPara("orderNo").trim().isEmpty()){
			sql += " and tor.order_no ='"+getPara("orderNo").trim()+"' ";
		}
		// 手机
		if(getPara("phoneNumber")!=null&& !getPara("phoneNumber").trim().isEmpty()){
			sql += " and tu.phone_number ='"+getPara("phoneNumber").trim()+"' ";
		}
		// 开始时间
		if(getPara("start_time")!=null&& !getPara("start_time").trim().isEmpty()){
			sql += " and unix_timestamp(tpl.create_time) >= unix_timestamp('"+getPara("start_time").trim()+"') ";
		}
		// 结束时间
		if(getPara("end_time")!=null&& !getPara("end_time").trim().isEmpty()){
			sql += " and unix_timestamp(tpl.create_time) <= unix_timestamp('"+getPara("end_time").trim()+"') ";
		}
		
		Record rc = Db.findFirst(" select count(1) as total "+sql);
		// 计算需要导出的总数
		long num = rc.getLong("total");
		String fileName = "../webapps/"+getRequest().getContextPath()+"/temp/"+"交易日志"+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";
		//fileName = "E:"+File.separator+"交易日志"+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";		
		ExportExcel et = new ExportExcel();
		et.createWork(fileName);
		String[] header = { "订单编号", "交易编号", "旅城号", "旅城昵称", "手机号", "交易金额","支付方式","操作类型","交易状态","交易时间","错误原因" };
		String[] key = { "order_no", "tpp_trade_no","user_no", "nick_name", "phone_number", "fee_total","pay_type","operation","pay_status","create_time","note"};
		try{			
			// 遍历total
			
			for(long i =0;i<num/Constants.ROW_NUMBER+1;i++){
				String limit = "limit "+i*Constants.ROW_NUMBER+","+Constants.ROW_NUMBER+" ";
				List<Record>  lt  = Db.find(CommonSql.ORDER_LOG_SELECT_EXPORT+sql+limit);
				et.exportExcel(fileName,"xx", header,key, lt, i*Constants.ROW_NUMBER+1, i);
			}
				
		}catch(Exception e){
			e.getMessage();
		}
		renderFile(new File(fileName));
		
	}
}
