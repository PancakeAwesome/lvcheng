package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCorpsLeader<M extends BaseCorpsLeader<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setLeaderId(java.lang.String leaderId) {
		set("leader_id", leaderId);
	}

	public java.lang.String getLeaderId() {
		return get("leader_id");
	}

	public void setCorpsId(java.lang.String corpsId) {
		set("corps_id", corpsId);
	}

	public java.lang.String getCorpsId() {
		return get("corps_id");
	}

}
