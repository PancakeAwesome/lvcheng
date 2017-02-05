package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAdminAction<M extends BaseAdminAction<M>> extends Model<M> implements IBean {

	public void setActionId(java.lang.String actionId) {
		set("action_id", actionId);
	}

	public java.lang.String getActionId() {
		return get("action_id");
	}

	public void setActionName(java.lang.String actionName) {
		set("action_name", actionName);
	}

	public java.lang.String getActionName() {
		return get("action_name");
	}

	public void setUri(java.lang.String uri) {
		set("uri", uri);
	}

	public java.lang.String getUri() {
		return get("uri");
	}

	public void setHttpMethod(java.lang.String httpMethod) {
		set("http_method", httpMethod);
	}

	public java.lang.String getHttpMethod() {
		return get("http_method");
	}

	public void setEnabled(java.lang.String enabled) {
		set("enabled", enabled);
	}

	public java.lang.String getEnabled() {
		return get("enabled");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

}