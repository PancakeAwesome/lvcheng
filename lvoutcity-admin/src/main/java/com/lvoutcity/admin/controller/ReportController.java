/**
 * 
 */
package com.lvoutcity.admin.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.api.Constant;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;

/**
 * @author wj
 *
 */
public class ReportController extends BaseController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void user(){
		render("index-user.html");
	}
	
	
	/**
	 * 获取月份下拉列表。默认上月的数据加载
	 */
	public void userMonth() throws Exception{
		Map map = new HashMap();

		String sql_month ="SELECT   DISTINCT DATE_FORMAT(create_time, '%Y-%m') AS month_time "
				+ "FROM  t_user  WHERE 1=1";
		//获取当前月份
		sql_month += " and DATE_FORMAT(create_time, '%Y-%m') < '"+LvoutcityUtils.formateDate(Constants.DATE.DATE_MONTH)+"' ";
		
		sql_month += " order by month_time desc ";
		// 获取所有月份
		
		List list = renderListFromDb(Db.find(sql_month));
		if(list!=null && list.size()>0){
			//获取新增用户数
			renderJson(list);
		}else{
			return;
		}
		
	}
	
	/**
	 * 获取新增用户数和用户总数
	 */
	public void getToalUser() throws Exception{
		Map map = new HashMap();
		String month = getPara("month");
		if(!LvoutcityUtils.isNotNull(month)){
			return;
		}
		// 新增用户数
		String sql_new = "SELECT COUNT(1) AS total_new FROM t_user WHERE 1=1 ";
		sql_new += " and DATE_FORMAT(create_time, '%Y-%m') ='"+month+"' ";
		Record rc = Db.findFirst(sql_new);
		map.put("total_new", rc.get("total_new"));
		
		// 用户总数
		String sql_all = "SELECT COUNT(1) AS total_all FROM t_user WHERE 1=1 ";
		sql_all += " and DATE_FORMAT(create_time, '%Y-%m') <='"+month+"' ";
		Record rc_ = Db.findFirst(sql_all);
		map.put("total_all", rc_.get("total_all"));
		
		// 新增用户具体数
		String sql_new_detail =" SELECT  COUNT(1) AS total, DATE_FORMAT(t1.create_time, '%d') AS day_time,"
				+ " (SELECT COUNT(1)  FROM t_user t2 WHERE DATE_FORMAT(t2.create_time, '%Y-%m-%d') <=DATE_FORMAT(t1.create_time, '%Y-%m-%d')) AS total_all "
				+ " FROM t_user t1 WHERE 1=1 ";
		sql_new_detail +=" and DATE_FORMAT(t1.create_time, '%Y-%m') ='"+month+"' ";
		sql_new_detail +=" group by day_time order by day_time ";
		List<Record> list1 = Db.find(sql_new_detail);
		if(list1!=null && list1.size()>0){
			// 每日新增用户数
			List data1 = new ArrayList<>();
			// 用户总数
			List data2 = new ArrayList<>();
			//日期
			List cate = new ArrayList<>();
			// 遍历封装数组
			for(Record rd :list1){
				data1.add(rd.get("total"));
				data2.add(rd.get("total_all"));
				cate.add(rd.get("day_time")+"日");
			}
			map.put("data1", data1);
			map.put("data2", data2);
			map.put("cate", cate);
		}
		renderJson(map);
	}

	

	public void sale(){
		render("index-sale.html");
	}
	
	/**
	 * 获取月份下拉列表。默认上月的数据加载
	 */
	public void saleMonth() throws Exception{
		Map map = new HashMap();

		String sql_month ="SELECT   DISTINCT DATE_FORMAT(pay_time, '%Y-%m') AS month_time "
				+ "FROM  t_order_record t1  WHERE is_delete = '1' ";
		sql_month += CommonSql.ORDER_STATUS;
		//获取当前月份
		sql_month += " and DATE_FORMAT(pay_time, '%Y-%m') < '"+LvoutcityUtils.formateDate(Constants.DATE.DATE_MONTH)+"' ";
		
		sql_month += " order by month_time desc ";
		// 获取所有月份
		
		List list = renderListFromDb(Db.find(sql_month));
		if(list!=null && list.size()>0){
			//获取新增用户数
			renderJson(list);
		}else{
			return;
		}
		
	}
	
	public void getToalSale() throws Exception{
		Map map = new HashMap();
		String month = getPara("month");
		if(!LvoutcityUtils.isNotNull(month)){
			return;
		}
		// 新增订单数
		String sql_new = "SELECT COUNT(1) AS total_all FROM t_order_record t1  WHERE is_delete='1' ";
		sql_new += CommonSql.ORDER_STATUS;
		sql_new += " and DATE_FORMAT(pay_time, '%Y-%m') ='"+month+"' ";
		Record rc = Db.findFirst(sql_new);
		map.put("total_all", rc.get("total_all"));
		
		// 订单金额
		String sql_all = "SELECT sum(fee_total) AS total_new FROM t_order_record t1 WHERE is_delete='1' ";
		sql_all += CommonSql.ORDER_STATUS;
		sql_all += " and DATE_FORMAT(pay_time, '%Y-%m') <='"+month+"' ";
		Record rc_ = Db.findFirst(sql_all);
		map.put("total_new", rc_.get("total_new"));
		
		// 新增具体数
		String sql_new_detail =" SELECT sum(fee_total) AS total, DATE_FORMAT(pay_time, '%d') AS day_time "
				+ " FROM t_order_record t1 WHERE is_delete='1' ";
		sql_new_detail += CommonSql.ORDER_STATUS;
		sql_new_detail +=" and DATE_FORMAT(pay_time, '%Y-%m') ='"+month+"' ";
		sql_new_detail +=" group by day_time order by day_time ";
		List<Record> list1 = Db.find(sql_new_detail);
		if(list1!=null && list1.size()>0){
			// 每日订单金额
			List data1 = new ArrayList<>();
			// 用户总数
			//日期
			List cate = new ArrayList<>();
			// 遍历封装数组
			for(Record rd :list1){
				data1.add(rd.get("total"));
				cate.add(rd.get("day_time")+"日");
			}
			map.put("data1", data1);
			map.put("cate", cate);
		}
		renderJson(map);
	}

	/**
	 * 查询销售明细
	 * @throws Exception
	 */
	public void selectSale() throws Exception{
		String month = getPara("month");
		
		String sql=" from (SELECT COUNT(1) AS order_total,t4.route_no,t4.route_name,SUM(t4.fee_total) AS fee_total ";
		String from =" FROM ( "
				+ " SELECT t1.fee_total,t3.route_no,t3.route_name FROM t_order_record t1 LEFT JOIN t_corps t2 ON t1.corps_id = t2.id "
				+ " LEFT JOIN t_route_mag t3 ON t2.route_id = t3.id WHERE t1.is_delete='1' "
				+ " ";
		from += CommonSql.ORDER_STATUS;
		from += " and DATE_FORMAT(pay_time, '%Y-%m') ='"+month+"' ";
		from += " ) t4 GROUP BY  t4.route_no ) t ";
		
		/*List<Record> list = Db.find(sql+from);
		renderJson(renderListFromDb(list));*/
		
		Page pg = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), "select * ", sql+from);
		renderDatatableFromDb(pg);
		
		
	}
	
	public void order(){
		render("index-order.html");
	}
	
	/**
	 * 订单数月份
	 */
	public void orderMonth(){
		Map map = new HashMap();

		String sql_month ="SELECT   DISTINCT DATE_FORMAT(order_time, '%Y-%m') AS month_time "
				+ "FROM  t_order_record  WHERE is_delete = '1' ";
		//sql_month += CommonSql.ORDER_STATUS;
		//获取当前月份
		sql_month += " and DATE_FORMAT(order_time, '%Y-%m') < '"+LvoutcityUtils.formateDate(Constants.DATE.DATE_MONTH)+"' ";
		
		sql_month += " order by month_time desc ";
		// 获取所有月份
		
		List list = renderListFromDb(Db.find(sql_month));
		if(list!=null && list.size()>0){
			
			renderJson(list);
		}else{
			return;
		}
		
	}
	
	public void getTotalOrder() throws Exception{
		Map map = new HashMap();
		DecimalFormat de = new DecimalFormat("0.00");
		//月份
		String month = getPara("month");
		if(!LvoutcityUtils.isNotNull(month)){
			return;
		}
		// 类型
		String type = getPara("type");
		if(!LvoutcityUtils.isNotNull(type)){
			return;
		}
		// 访问量
		String sql_new = "SELECT sum(count) AS total FROM t_page_view WHERE 1=1 ";
		sql_new += " and DATE_FORMAT(date_time, '%Y-%m') ='"+month+"' ";
		Record rc = Db.findFirst(sql_new);
		map.put("view_total", rc.get("total"));
		
		// 订单数
		String sql_all = "SELECT count(1) AS total FROM t_order_record WHERE is_delete='1' ";
		sql_all += " and DATE_FORMAT(order_time, '%Y-%m') <='"+month+"' ";
		Record rc_ = Db.findFirst(sql_all);
		map.put("order_total", rc_.get("total"));
		
		// 转化率
		double ss = Integer.parseInt(rc.get("total").toString());
		if(ss<=0){
			map.put("zhuan_hua", 0+"%");
		}else{
			double ss_ = Integer.parseInt(rc_.get("total").toString());
			
			map.put("zhuan_hua", de.format(ss_/ss)+"%");
		}
		
		
		
		// 成交订单数
		String sql = "SELECT count(1) AS total FROM t_order_record t1 WHERE is_delete='1' ";
		sql +=  CommonSql.ORDER_STATUS;
		sql += " and DATE_FORMAT(pay_time, '%Y-%m') <='"+month+"' ";
		Record rcd = Db.findFirst(sql);
		map.put("order_good", rcd.get("total"));
		
		String sql1 =" SELECT count, DATE_FORMAT(date_time, '%d') AS day_time "
				+ " FROM t_page_view WHERE 1=1 ";
		sql1 +=" and DATE_FORMAT(date_time, '%Y-%m') ='"+month+"' ";
		sql1 +=" group by day_time order by day_time ";
		
		String sql2 =" SELECT count(1) AS total, DATE_FORMAT(order_time, '%d') AS day_time "
				+ " FROM t_order_record WHERE is_delete='1' ";
		//sql += CommonSql.ORDER_STATUS;
		sql2 +=" and DATE_FORMAT(order_time, '%Y-%m') ='"+month+"' ";
		sql2 +=" group by day_time order by day_time ";
		
		// 访问量
		if("1".equals(type)){
			
			List<Record> list1 = Db.find(sql1);
			if(list1!=null && list1.size()>0){
				// 每日访问量
				List data1 = new ArrayList<>();
				//日期
				List cate = new ArrayList<>();
				// 遍历封装数组
				for(Record rd :list1){
					data1.add(rd.get("count"));
					cate.add(rd.get("day_time")+"日");
				}
				map.put("data1", data1);
				map.put("cate", cate);
			}
			
		}else if("2".equals(type)){ // 订单数 
			// 新增订单数
			
			List<Record> list1 = Db.find(sql2);
			if(list1!=null && list1.size()>0){
				// 每日订单数
				List data1 = new ArrayList<>();
				//日期
				List cate = new ArrayList<>();
				// 遍历封装数组
				for(Record rd :list1){
					data1.add(rd.get("total"));
					cate.add(rd.get("day_time")+"日");
				}
				map.put("data1", data1);
				map.put("cate", cate);
			}
		}else{ // 转化率
			sql="";
			sql = "select t2.total,t1.count,t1.day_time  from ("+sql1+") as t1 left join ("+sql2+") as t2 on t1.day_time = t2.day_time";
			List<Record> list1 = Db.find(sql);
			if(list1!=null && list1.size()>0){
				// 每日订单金额
				List data1 = new ArrayList<>();
				// 用户总数
				//日期
				List cate = new ArrayList<>();
				// 遍历封装数组
				for(Record rd :list1){
					double cc=0.0;
					if(rd.get("total")!=null){
						cc = Integer.parseInt(rd.get("total").toString());
					}
					double cc_ = Integer.parseInt(rd.get("count").toString());
					data1.add(de.format(cc/cc_)+"%");
					cate.add(rd.get("day_time")+"日");
				}
				map.put("data1", data1);
				map.put("cate", cate);
			}
		}
		
		
		renderJson(map);
	}
	
	/**
	 * 订单明细
	 * @throws Exception
	 */
	public void selectOrder() throws Exception{
		String month = getPara("month");
		
		// 获取线路名称，订单数
		String sql1 = " SELECT COUNT(1) AS order_total,t2.route_id "
				+ " FROM t_order_record t1 LEFT JOIN t_corps t2 ON t1.corps_id = t2.id "
				+ "LEFT JOIN t_route_mag t3 ON t2.route_id = t3.id WHERE t1.is_delete='1' ";
		sql1 +=" and DATE_FORMAT(t1.order_time, '%Y-%m') ='"+month+"' ";
		sql1+= " GROUP BY t2.route_id ";
		
		// 获取线路访问量
		String sql2=" SELECT SUM(t4.count) AS count,t4.route_id,t3.route_no,t3.route_name FROM t_page_view t4 "
				+ "LEFT JOIN t_route_mag t3 ON t4.route_id = t3.id WHERE 1=1 ";
		sql2 +=" and DATE_FORMAT(t4.date_time, '%Y-%m') ='"+month+"' ";
		sql2 += " GROUP BY t4.route_id ";
		
		// 获取有效订单数
		String sql3 = " SELECT COUNT(1) AS order_good,t2.route_id "
				+ " FROM t_order_record t1 LEFT JOIN t_corps t2 ON t1.corps_id = t2.id "
				+ " LEFT JOIN t_route_mag t3 ON t2.route_id = t3.id WHERE t1.is_delete='1'  ";
		sql3 +=" and DATE_FORMAT(t1.pay_time, '%Y-%m') ='"+month+"' ";
		sql3 += CommonSql.ORDER_STATUS;
		sql3 +=" GROUP BY t2.route_id ";
		
		String sql =" select IFNULL(tt1.count,1) as count ,tt1.route_id,tt1.route_no,tt1.route_name,"
				+ " IFNULL(tt2.order_total,0) as order_total ,IFNULL(tt3.order_good,0) as  order_good,"
				+ "CONCAT(FORMAT(IFNULL(tt2.order_total,0)/IFNULL(tt1.count,1)*100,2),'%') as zhuan ";
		String from =" from ( "+sql2+" ) as tt1 left join ( "+sql1+" ) as tt2 on tt1.route_id = tt2.route_id "
				+ " left join ( "+sql3+" ) as tt3 on tt1.route_id = tt3.route_id  ";
		Page pg = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), sql, from);
		renderDatatableFromDb(pg);
		
		
	}
}
