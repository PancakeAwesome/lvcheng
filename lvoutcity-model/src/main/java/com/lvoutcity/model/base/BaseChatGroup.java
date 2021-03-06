package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseChatGroup<M extends BaseChatGroup<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setGroupName(java.lang.String groupName) {
		set("group_name", groupName);
	}

	public java.lang.String getGroupName() {
		return get("group_name");
	}

	public void setGroupAvatar(java.lang.String groupAvatar) {
		set("group_avatar", groupAvatar);
	}

	public java.lang.String getGroupAvatar() {
		return get("group_avatar");
	}

	public void setRouteId(java.lang.String routeId) {
		set("route_id", routeId);
	}

	public java.lang.String getRouteId() {
		return get("route_id");
	}

	public void setMsgCount(java.lang.String msgCount) {
		set("msg_count", msgCount);
	}

	public java.lang.String getMsgCount() {
		return get("msg_count");
	}

	public void setSystemGroup(java.lang.String systemGroup) {
		set("system_group", systemGroup);
	}

	public java.lang.String getSystemGroup() {
		return get("system_group");
	}

	public void setCreatorId(java.lang.String creatorId) {
		set("creator_id", creatorId);
	}

	public java.lang.String getCreatorId() {
		return get("creator_id");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setCreateUser(java.lang.String createUser) {
		set("create_user", createUser);
	}

	public java.lang.String getCreateUser() {
		return get("create_user");
	}

	public void setGuideId(java.lang.String guideId) {
		set("guide_id", guideId);
	}

	public java.lang.String getGuideId() {
		return get("guide_id");
	}

	public void setCorpsId(java.lang.String corpsId) {
		set("corps_id", corpsId);
	}

	public java.lang.String getCorpsId() {
		return get("corps_id");
	}

}
