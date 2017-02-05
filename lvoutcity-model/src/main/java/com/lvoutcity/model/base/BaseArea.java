package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseArea<M extends BaseArea<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setParentId(java.lang.Long parentId) {
		set("parent_id", parentId);
	}

	public java.lang.Long getParentId() {
		return get("parent_id");
	}

	public void setLft(java.lang.Long lft) {
		set("lft", lft);
	}

	public java.lang.Long getLft() {
		return get("lft");
	}

	public void setRgt(java.lang.Long rgt) {
		set("rgt", rgt);
	}

	public java.lang.Long getRgt() {
		return get("rgt");
	}

}