package com.lvoutcity.model;

import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.base.BaseSet;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Set extends BaseSet<Set> {
	public static final Set dao = new Set();
	public boolean update(String userId,String status,String type){
		return Set.dao.findFirst("select * from t_set where user_id= ?",userId).set(type,status).update();
	}
	
	/**
	 * 获取开关信息
	 * @param userId
	 * @return
	 */
	public Set querySet(String userId){
		Set s =  Set.dao.findFirst("select * from t_set where user_id='"+userId+"'");
		if(s==null){
			s = new Set();
			s.setId(LvoutcityUtils.getUUID());
			s.setUserId(userId);
			s.setAttraState("0");
			s.setChatState("0");
			s.save();
		}
		return s;
	}
}
