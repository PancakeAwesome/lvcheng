package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAttention<M extends BaseAttention<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setRouteNo(java.lang.Integer routeNo) {
		set("route_no", routeNo);
	}

	public java.lang.Integer getRouteNo() {
		return get("route_no");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setConcernTime(java.util.Date concernTime) {
		set("concern_time", concernTime);
	}

	public java.util.Date getConcernTime() {
		return get("concern_time");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

}
