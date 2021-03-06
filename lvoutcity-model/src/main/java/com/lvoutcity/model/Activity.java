package com.lvoutcity.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.base.BaseActivity;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Activity extends BaseActivity<Activity> {
	public static final Activity dao = new Activity();
	
	public static Activity getInviterActivity(){
		Activity a = dao.findFirst("select * from t_activity where target = '"+Constants.ACTIVITY.TARGET_INVITER+"'");
		if(a==null){
			a = new Activity();
			a.setId(LvoutcityUtils.getUUID());
			a.setTarget(Constants.ACTIVITY.TARGET_INVITER);
			a.save();
		}
		return a;
	}
	public static Activity getRegisterActivity(){
		Activity a = dao.findFirst("select * from t_activity where target = '"+Constants.ACTIVITY.TARGET_REGISTER+"'");
		if(a==null){
			a = new Activity();
			a.setId(LvoutcityUtils.getUUID());
			a.setTarget(Constants.ACTIVITY.TARGET_REGISTER);
			a.save();
		}
		return a;
	}
	
	public boolean inDate(){
		Date todayDate = new Date();
		if(getStartDate().before(todayDate)&&getEndDate().after(todayDate)){
			//不在活动时间
			return true;
		}else{
			return false;
		}
	}
	
	public boolean inQuota(){
		if(getQuota()==0) // 0 即无限
			return true;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE.DATE_ALL);

		int num = UserMember.dao.findRegisterNumByDate(sdf.format(getStartDate()),
				sdf.format(getEndDate()));
		if (num <= getQuota())//是否超过人数
			return true;
		else
			return false;
	}
	
}
