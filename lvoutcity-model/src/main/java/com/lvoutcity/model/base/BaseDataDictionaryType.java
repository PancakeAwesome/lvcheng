package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDataDictionaryType<M extends BaseDataDictionaryType<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setSuperId(java.lang.String superId) {
		set("super_id", superId);
	}

	public java.lang.String getSuperId() {
		return get("super_id");
	}

	public void setValue(java.lang.String value) {
		set("value", value);
	}

	public java.lang.String getValue() {
		return get("value");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setCreateUser(java.lang.String createUser) {
		set("create_user", createUser);
	}

	public java.lang.String getCreateUser() {
		return get("create_user");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

	public void setUpdateUser(java.lang.String updateUser) {
		set("update_user", updateUser);
	}

	public java.lang.String getUpdateUser() {
		return get("update_user");
	}

	public void setIsDelete(java.lang.String isDelete) {
		set("is_delete", isDelete);
	}

	public java.lang.String getIsDelete() {
		return get("is_delete");
	}

	public void setTCode(java.lang.String tCode) {
		set("t_code", tCode);
	}

	public java.lang.String getTCode() {
		return get("t_code");
	}

}