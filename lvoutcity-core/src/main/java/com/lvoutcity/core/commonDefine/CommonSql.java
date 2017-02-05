/**
 * 
 */
package com.lvoutcity.core.commonDefine;

import com.lvoutcity.core.util.Constants;

/**
 * @author wj
 *
 */
public class CommonSql {

	// 领队管理联合查询需要字段
	public static String LEADER_SELECT = " select tu.*,"
			+ "tue.province_id,tue.city_id,tue.leader_status as status, "
			+ "ttc.club_short_name,"
			+ "tue.personal_sign,date_format(tue.create_time,'%Y-%m-%d %T') as create_time ,"
			+ "(select phone_number from t_user t1 where t1.id = tue.referral) as referral_phone_number,"
			+ "(select user_name from t_user_backgroup t2 where t2.user_id = tu.create_user  ) as create_user_name  ";

	public static String GROUP_SELECT = " select tp.id,tp.group_name,tp.club_id,tb.club_name  ";
	public static String PUBLIC_SELECT = " select t1.id,t1.club_id,t1.title,t1.title_img,t1.content,"
			+ "t1.zhai_yao,t1.creative_id,t1.status,t1.create_user,t1.is_delete,t1.update_time,"
			+ "t1.send_time,"
			+ "DATE_FORMAT(t1.create_time, '%Y-%m-%d %T')as create_time,"
			+ "t1.update_user,(select user_name from t_user_backgroup t2 where t2.user_id = t1.create_user) as create_user_name  ";
	public static String ORDER_LOG_SELECT ="SELECT  tpl.tpp_trade_no, tpl.pay_type,tpl.fee_gold,tpl.fee_cash,tpl.ttp_error_code,DATE_FORMAT(tpl.create_time, '%Y-%m-%d %T')as create_time,"
			+ " tpl.pay_status,tpl.note,tpl.operation,tor.order_no,tor.fee_total,tu.phone_number,tu.nick_name,tu.user_no";
	public static String ORDER_LOG_SELECT_EXPORT ="SELECT  tpl.tpp_trade_no, "
			+ "case when tpl.pay_type='1' then '支付宝' when tpl.pay_type='2' then '微信' when tpl.pay_type='3' then '金币' else '' end as pay_type,"
			+ "tpl.fee_gold,tpl.fee_cash,tpl.ttp_error_code,DATE_FORMAT(tpl.create_time, '%Y-%m-%d %T')as create_time,"
			+ " case when tpl.pay_status='1' then '成功' else '失败' end as pay_status,tpl.note,"
			+ "case when tpl.operation='0' then '支付' else '退款' end as operation,tor.order_no,tor.fee_total,tu.phone_number,tu.nick_name,tu.user_no";
	
	public static String OPERATION_LOG_SELECT ="SELECT tl.action,tl.result,DATE_FORMAT(tl.create_time, '%Y-%m-%d %T')as create_time,tl.remark,tu.user_no,tb.user_name ";
	public static String OPERATION_LOG_SELECT_EXPORT ="SELECT tl.action,case when tl.result='1' then '成功' else '失败' end as result ,DATE_FORMAT(tl.create_time, '%Y-%m-%d %T')as create_time,tl.remark,tu.user_no,tb.user_name ";
	public static String ORDER_STATUS =" and t1.status in ("+Constants.ORDER.STATUS_PAID+","+Constants.ORDER.STATUS_ON_TRIP+","+Constants.ORDER.STATUS_TRIP_FINISH+") ";

}
