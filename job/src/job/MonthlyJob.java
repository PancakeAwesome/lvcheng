package job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.Account;

/**
 * @author wss
 */

public class MonthlyJob {

	
	/**
	 * 账单生成
	 */
	@JobDescription("账单生成")
	@JobOrder(0)
	public static void createAccount() {
		Date month[] = getMonthFirstAndLastDay();
		List<Record> rs = Account.dao.getNotAccountedClubs(month[1]);
		for (Record r : rs) {
			String clubId = r.get("id").toString();		
		
			Account record = new Account();
			String id = LvoutcityUtils.getUUID();
			record.setAccountId(id);
			record.setCreateDate(new Date());
			record.setStartDate(month[0]);
			record.setEndDate(month[1]);
			record.setAccountStatus("0");
			record.setClubId(clubId);
			record.setPaymentMode("0");
			record.save();
			record.setAccountAmount(Account.dao.bindAccountAndReturnAmount(clubId, id,month[1]));
			record.update();			
		}
	}
	
	private static Date[] getMonthFirstAndLastDay() {
		Date month[] = new Date[2]; 
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        month[0] = c.getTime();
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();  
        ca.add(Calendar.MONTH, -1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        month[1] = ca.getTime();
        
        // for test
        month[1] = new Date();
		return month;
	}
}
