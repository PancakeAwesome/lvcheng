package com.lvoutcity.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGuideMarkerLine<M extends BaseGuideMarkerLine<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setMarker(java.lang.String marker) {
		set("marker", marker);
	}

	public java.lang.String getMarker() {
		return get("marker");
	}

	public void setPolyline(java.lang.String polyline) {
		set("polyline", polyline);
	}

	public java.lang.String getPolyline() {
		return get("polyline");
	}

	public void setMapInfo(java.lang.String mapInfo) {
		set("map_info", mapInfo);
	}

	public java.lang.String getMapInfo() {
		return get("map_info");
	}

	public void setGuideId(java.lang.String guideId) {
		set("guide_id", guideId);
	}

	public java.lang.String getGuideId() {
		return get("guide_id");
	}

}