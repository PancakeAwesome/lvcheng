/**
 * 
 */
package com.lvoutcity.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lvoutcity.core.util.LvoutcityUtils.isNotNull;

/**
 * 
 * @ClassName: Node 
 * @Description: 帮助类
 * @author James w.s
 * @date 2016年5月19日 下午5:31:03 
 *
 */
public class ZTree implements java.io.Serializable{
	
	private static final long serialVersionUID = -1042776287207466140L;

	public static final String	URL = "url";
	
	public static final String 	ORDER_TYPE = "orderType";
	
	public static final String 	ROLE_MARK = "roleMark";
	
	public static final String 	ICON_CLS = "iconCls";
	
	private String		id;
	
	private String		name;
	
	private String		state;
	
	private boolean		checked;
	
	private String 		superId;
	
	private boolean 	roleMark = false;
	
	private Map<String,Object> attributes = new HashMap<String, Object>();
	
	private List<ZTree>  children = new ArrayList<ZTree>();
	
	public ZTree(){
		
	}
	
	public ZTree(String id, String name, String superId, String url, String orderType){
		this.id = id;
		this.name = name;
		this.superId = superId;
		if(isNotNull(url)){
			this.attributes.put(URL, url);
		}
		if(isNotNull(orderType)){
			this.attributes.put(ORDER_TYPE, orderType);
		}
	}
	
	public ZTree(String id, String name, String superId, String url, String orderType, Integer roleMark, String iconCls){
		this.id = id;
		this.name = name;
		this.superId = superId;
		if(isNotNull(url)){
			this.attributes.put(URL, url);
		}
		if(isNotNull(orderType)){
			this.attributes.put(ORDER_TYPE, orderType);
		}
		if(isNotNull(roleMark)){
			this.roleMark = true;
		}
		if(isNotNull(iconCls)){
			this.attributes.put(ICON_CLS, iconCls);
		}
	}
	
	public ZTree(String id, String name, String superId, String url, Integer roleMark){
		this.id = id;
		this.name = name;
		this.superId = superId;
		if(isNotNull(url)){
			this.attributes.put(URL, url);
		}
		if(isNotNull(roleMark)){
			this.attributes.put(ROLE_MARK, roleMark);
		}
	}
	
	public ZTree(String id, String name, String superId, String url, Integer roleMark, String iconCls){
		this.id = id;
		this.name = name;
		this.superId = superId;
		if(isNotNull(url)){
			this.attributes.put(URL, url);
		}
		if(isNotNull(roleMark)){
			this.attributes.put(ROLE_MARK, roleMark);
		}
		if(isNotNull(iconCls)){
			this.attributes.put(ICON_CLS, iconCls);
		}
	}
	
	
	public ZTree(String id, String name, String superId, String url){
		this.id = id;
		this.name = name;
		this.superId = superId;
		this.attributes.put(URL, url);
	}
	
	public ZTree(String id, String name, String superId){
		this.id = id;
		this.name = name;
		this.superId = superId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<ZTree> getChildren() {
		return children;
	}

	public void setChildren(List<ZTree> children) {
		this.children = children;
	}

	public String getSuperId() {
		return superId;
	}

	public void setSuperId(String superId) {
		this.superId = superId;
	}
	
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	public String toString() {
		if(isNotNull(this.attributes.get(ROLE_MARK))){
			return this.attributes.get(ROLE_MARK).toString();
		}
		return id;
	}

	public boolean isRoleMark() {
		return roleMark;
	}

	public void setRoleMark(boolean roleMark) {
		this.roleMark = roleMark;
	}
}
