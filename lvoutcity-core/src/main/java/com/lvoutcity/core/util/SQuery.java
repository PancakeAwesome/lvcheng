package com.lvoutcity.core.util;

import static com.lvoutcity.core.util.LvoutcityUtils.isAllNotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @ClassName: SQuery 
 * @Description: SQL 
 * @author James w.s
 * @date 2016年5月13日 上午11:33:18 
 *
 */
public class SQuery {
	
	private StringBuilder sQuery;
	
	private List<String> params = new ArrayList<String>();			//查询变量名
	
	private List<Object> values = new ArrayList<Object>();			//查询值
	
	private	int				index;                                  //索引值
	
	/**
	 * 
	 * @Title: SQuery 
	 * @Description: 创建SQL语句 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午11:34:33
	 * @create_user James w.s
	 * @throws
	 */
	public static SQuery create(){
		return new SQuery();
	}
	
	/**
	 * 
	 * @Title: create 
	 * @Description: 定义SQL 语言 
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return SQuery    返回类型 
	 * @create_time 2016下午1:18:34
	 * @create_user James w.s
	 * @throws
	 */
	public static SQuery create(String sql){
		return new SQuery(sql);
	}

	/**
	 * 
	 * @Title: getSQuery 
	 * @Description: 获取sql语句
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016下午2:29:41
	 * @create_user James w.s
	 * @throws
	 */
	public String getSQuery(){
		return this.sQuery.toString();
	}
	
	public SQuery(){
		sQuery = new StringBuilder();
		params.clear();
		values.clear();
		index = 0;
	}
	
	public SQuery(String str){
		sQuery = new StringBuilder(str);
		params.clear();
		values.clear();
		index = 0;
	}
	
	public SQuery append(String str){
		sQuery.append(str);
		return this;
	}
	
	/**
	 * 
	 * @Title: isFrom 
	 * @Description: 获取from表单语句
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016下午1:27:23
	 * @create_user James w.s
	 * @throws
	 */
	public String isFrom(){
		int index = this.getsQuery().lastIndexOf("from");
		if(index != -1){
			return this.getsQuery().substring(index,this.getsQuery().length());
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: isSql 
	 * @Description: 获取查询条件语句
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016下午1:28:53
	 * @create_user James w.s
	 * @throws
	 */
	public String isSql(){
		int index = this.getsQuery().lastIndexOf("from");
		if(index != -1){
			return this.getsQuery().substring(0,index);
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: isOrderBy 
	 * @Description: 获取查询order by 语句
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016下午1:31:54
	 * @create_user James w.s
	 * @throws
	 */
	public String isOrderBy(){
		int index = this.getsQuery().lastIndexOf(" order by ");
		if(index != -1){
			return this.getsQuery().substring(index,this.getsQuery().length());
		}
		return null;
	}

	/**
	 * 
	 * @Title: setParam 
	 * @Description: set param value
	 * @param @param paramName
	 * @param @param value
	 * @param @return    设定文件 
	 * @return HQuery    返回类型 
	 * @create_time 2016下午1:44:54
	 * @create_user James w.s
	 * @throws
	 */
	public SQuery setParam(String paramName, Object value){
		if(isAllNotNull(paramName,value)){
			params.add(paramName);
			values.add(value);
		}
		return this;
	}
	
	/**
	 * 
	 * @Title: between 
	 * @Description: between 
	 * @param @param key
	 * @param @param value1
	 * @param @param value2
	 * @param @return    设定文件 
	 * @return SQuery    返回类型 
	 * @create_time 2016下午1:52:33
	 * @create_user James w.s
	 * @throws
	 */
	public SQuery between(String key,Object value1,Object value2){
		this.append(" and ");
		this.append(key);
		this.append(" between :");
		this.append("  ");
		return null;
	}
	
/*	private String getParamName(String key){
		if(key.contains("(")){
			return "";
		}
		if(key.contains(".")){
			return key.substring(key.lastIndexOf(".")+1);
		}
		return key;
	}*/
	
	
	
	public StringBuilder getsQuery() {
		return sQuery;
	}

	public void setsQuery(StringBuilder sQuery) {
		this.sQuery = sQuery;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	
	
	
}
