package job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.druid.DruidPlugin;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.OrderRecord;
import com.lvoutcity.model.UserMember;
import com.lvoutcity.model.UserWallet;
import com.lvoutcity.model.Wallet;
import com.lvoutcity.model._MappingKit;

/**
 * @author wss
 */

public class DailyJob {
	
	public static Date today;
	static {
		today = new Date();
	}
	
	/**
	 * 积分结算
	 * 
	 */
	@JobDescription("积分结算")
	@JobOrder(0)
	public static void pointsCheckOut(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today_date_str = sdf.format(today);
		List<OrderRecord> orders = OrderRecord.dao.find("select * from t_order_record "
				+ " where is_delete='1' and status = '"+Constants.ORDER.STATUS_ON_TRIP+"'"
				+ " and end_time < DATE_FORMAT('"+today_date_str+"','%Y-%m-%d')"); 
		for(OrderRecord o:orders){
			UserMember um = UserMember.dao.findByUserId(o.getCreateUser());
			um.setTotalPoint(um.getTotalPoint()+(int)Math.floor(o.getFeeTotal().doubleValue()));
			um.update();
		}
	}
	
	/**
	 * 金币抵扣取消(因为支付超时而产生的)
	 */
	@JobDescription("超时订单金币抵扣取消")
	@JobOrder(1)
	public static void payGoldCancel() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE.DATE_ALL);
		Calendar cld = Calendar.getInstance();
		cld.setTime(today);
		cld.add(Calendar.DAY_OF_MONTH, -1);
		String day_before_str = sdf.format(cld.getTime());
		
		List<Wallet> lw = Wallet.dao.find(
				"select w.* "
				+ " from t_wallet w left join t_order_record ord "
				+ " on  ord.id = w.order_id "
				+ " where w.is_delete = '1' and w.status = ? and ord.status = ? "
				+ " and order_time < DATE_FORMAT('"+day_before_str+"','%Y-%m-%d %T')",
				Constants.WALLET.STATUS_OUT,
				Constants.ORDER.STATUS_UNPAID);
		List<UserWallet> luw = new ArrayList<UserWallet>();
		lw.forEach(w->{
			UserWallet uw = UserWallet.dao.findByUserId(w.getUserId());
			w.setId(LvoutcityUtils.getUUID());
			w.setStatus(Constants.WALLET.STATUS_IN);
			w.setCreateTime(null);
			w.setSubject("订单支付超时，金币退回");
			w.setBalance(uw.add(w.getMoney()));
			uw.update();
		});
		Db.batchSave(lw, lw.size());
		Db.batchUpdate(luw, luw.size());
	}
	
	/**
	 * 更新订单状态
	 */
	@JobDescription("更新订单状态")
	@JobOrder(2)
	public static void orderStatusUpdate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today_date_str = sdf.format(today);
		// on trip
		Db.update("update t_order_record set status = '"+Constants.ORDER.STATUS_ON_TRIP
				+ "' where  is_delete='1' and  status = '"+Constants.ORDER.STATUS_PAID+"'"
				+ " and start_time <= DATE_FORMAT('"+today_date_str+"','%Y-%m-%d')");
		//trip end
		Db.update("update t_order_record set status = '"+Constants.ORDER.STATUS_TRIP_FINISH
				+ "' where  is_delete='1' and  status = '"+Constants.ORDER.STATUS_ON_TRIP+"'"
				+ " and end_time < DATE_FORMAT('"+today_date_str+"','%Y-%m-%d')");
		//payment timeout
		sdf.applyPattern(Constants.DATE.DATE_ALL);
		Calendar cld = Calendar.getInstance();
		cld.setTime(today);
		cld.add(Calendar.DAY_OF_MONTH, -1);
		String day_before_str = sdf.format(cld.getTime());
		
		Db.update("update t_order_record set status = '"+Constants.ORDER.STATUS_CANCLED_BACK
				+ "' where status = '"+Constants.ORDER.STATUS_UNPAID+"'"
				+ " and  is_delete='1' and order_time < DATE_FORMAT('"+day_before_str+"','%Y-%m-%d %T')");
		
	}
	/**
	 * 分润正式入账
	 */
	@JobDescription("分润正式入账")
	@JobOrder(3)
	public static void shareGoldCheckOut(){
		List<Wallet> wl = Wallet.dao.find("select w.* from  t_wallet w left join t_order_record o on o.id = w.order_id "
				+ "where  o.is_delete='1' and  o.status = '"+Constants.ORDER.STATUS_TRIP_FINISH+"' "
				+ "and w.status = '"+Constants.WALLET.STATUS_PRE_IN+"'");
		for(Wallet w : wl){
			UserWallet uw = UserWallet.dao.findByUserId(w.getUserId());
			uw.add(w.getMoney());
			uw.update();
			w.setStatus(Constants.WALLET.STATUS_IN);
			w.setBalance(uw.getMoney());
			w.update();
		}
//		Db.batchUpdate(wl, wl.size());
	}
	/**
	 * 分润取消
	 */
	@JobDescription("分润取消")
	@JobOrder(4)
	public static void shareGoldCancel(){
		
		Db.update("update t_wallet w left join t_order_record o on o.id = w.order_id "
				+ "set w.is_delete = '0'"
				+ "where  o.is_delete='1' and  o.status in ( '"+Constants.ORDER.STATUS_CANCLED_BACK+"','"+Constants.ORDER.STATUS_CANCLED_USER+"' ) "
				+ "and w.status = '"+Constants.WALLET.STATUS_PRE_IN+"'");
	}
	
	
	
	
	
	public static void main(String[] args){
		Prop p = PropKit.use("resources/config.props");
//		Prop p = PropKit.use("jdbc.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbc.url"), p.get("jdbc.user"), p.get("jdbc.password"),p.get("p.driverClassName"));
		druidPlugin.start();
		
		
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setShowSql(true);
		_MappingKit.mapping(activeRecordPlugin);
		activeRecordPlugin.start();
		
	}
	

	
}
