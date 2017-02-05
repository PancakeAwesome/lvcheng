package com.lvoutcity.core.api;

import com.jfinal.plugin.redis.Redis;

public class TokenCommon {
	

	public TokenBean getToken(String tokenBean) {
		Object tmpObj = Redis.use().hget("LoginTokens", tokenBean);
		TokenBean tBean = (TokenBean)tmpObj;
		return tBean;
	}
	
	
}
