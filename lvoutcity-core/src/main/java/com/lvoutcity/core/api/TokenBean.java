package com.lvoutcity.core.api;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class TokenBean {
	private String token;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date expire;
	private String userId;
	private String userMemberId;
	private  String rongYunToken;
	public String getToken() {
		return token;
	}
	public Date getExpire() {
		return expire;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserMemberId() {
		return userMemberId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserMemberId(String userMemberId) {
		this.userMemberId = userMemberId;
	}
	public String getRongYunToken() {
		return rongYunToken;
	}
	public void setRongYunToken(String rongYunToken) {
		this.rongYunToken = rongYunToken;
	}
	
}
