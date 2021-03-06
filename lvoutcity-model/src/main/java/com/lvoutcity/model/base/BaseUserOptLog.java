package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUserOptLog<M extends BaseUserOptLog<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setReferrerUrl(java.lang.String referrerUrl) {
		set("referrer_url", referrerUrl);
	}

	public java.lang.String getReferrerUrl() {
		return get("referrer_url");
	}

	public void setAction(java.lang.String action) {
		set("action", action);
	}

	public java.lang.String getAction() {
		return get("action");
	}

	public void setResult(java.lang.String result) {
		set("result", result);
	}

	public java.lang.String getResult() {
		return get("result");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

}
