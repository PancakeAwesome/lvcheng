package com.lvoutcity.core.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.core.util.Constants;

/** 
* @ClassName: BaseController 
* @Description: Controller 基类
* @author Gdh
* @date 2016年4月25日 下午12:44:00 
*  
*/
public class BaseController extends Controller{

	public Integer getDatatableStart(){
		return getParaToInt("start",0);
	}
	
	public Integer getDatatableLength(){
		return getParaToInt("length",20);
	}
	
	public String getDatatableOrderCol(){
		return getPara("columns["+ getParaToInt("order[0][column]")+"][data]");
	}
	
	public String getDatatableOrderDir(){
		return getPara("order[0][dir]");
	}
	
	public void renderDatatable(List<?> list){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", getParaToInt("draw"));
		map.put("recordsTotal", list.size());
		map.put("recordsFiltered", list.size());
		map.put("data", list);
		renderJson(map);
	}
	
	public void renderDatatableFromDb(List<?> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", getParaToInt("draw"));
		map.put("recordsTotal", list.size());
		map.put("recordsFiltered", list.size());
		List<Object> list_json = new ArrayList<Object>();
		for (int i = 0; i < list.size(); i++) {
			Object ob = JSON.parse(((Record) list.get(i)).toJson());
			list_json.add(ob);
		}
		map.put("data", list_json);
		renderJson(map);
	}
	
	public void renderDatatable(Page<?> page){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", getParaToInt("draw"));
		map.put("recordsTotal", page.getTotalRow());
		map.put("recordsFiltered", page.getTotalRow());
		map.put("data", page.getList());
		renderJson(map);
	}
	
	public void renderDatatableFromDb(Page<?> page){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", getParaToInt("draw"));
		map.put("recordsTotal", page.getTotalRow());
		map.put("recordsFiltered", page.getTotalRow());
		List<Object> list =new ArrayList<Object>();
		if(page.getList()!=null){
			for(int i = 0 ;i<page.getList().size();i++){
				Record record =(Record) page.getList().get(i);
				Object ob =  JSON.parse(record.toJson() );
				list.add(ob);
			};
		}
		
		map.put("data", list);
		renderJson(map);
	}
	
	public List<Object> renderListFromDb(List<?> list_){
		List<Object> list =new ArrayList<Object>();
		if(list_!=null&& list_.size()>0 ){
			for(int i = 0 ;i<list_.size();i++){
				Record record =(Record)list_.get(i);
				Object ob =  JSON.parse(record.toJson() );
				list.add(ob);
			};
		}
		return list;
	}
	
	@Override
	public <T> T getBean(Class<T> beanClass) {
		return super.getBean(beanClass,"");
	}
	
	@Override
	public <T> T getModel(Class<T> modelClass) {
		return super.getModel(modelClass,"");
	}
	
	public String getCurrentUserId(){
		return getSessionAttr("current_user_id");
	}
	
	
	/**
	 * 获取list map形式的传参
	 * 
	 * @param keyname
	 * @return
	 */
	public List<HashMap<String, String>> getListMap(String keyname){
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>(); 
		Map<String, String[]> parasMap = getRequest().getParameterMap();
		HashMap<String, String> m = null;
		String index = null;
		for (Entry<String, String[]> entry : parasMap.entrySet()) {
			String paraName = entry.getKey();
			if(paraName.startsWith(keyname+"[")){
				String i = paraName.substring(paraName.indexOf("[")+1,paraName.indexOf("]"));
				if(!i.equals(index)){
					index = i;
					if(m!=null)
						result.add(m);
					m = new HashMap<String, String>();
				}
				String realname = paraName.substring(paraName.lastIndexOf("[")+1,paraName.length()-1);
				if(entry.getValue()!=null)
					m.put(realname, entry.getValue()[0]);
			}
		}
		if(m!=null)
			result.add(m);
		return result;
	}

	@Override
	public HttpSession getSession() {

		// TODO Auto-generated method stub
		return super.getSession();
	}

	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return super.getSession(create);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionAttr(String key) {
		// TODO Auto-generated method stub
		Map<?, ?> session = Redis.use().get(getCookie(Constants.COOKIE_NAME).toString());
		return session != null ? (T)session.get(key) : null;
	}

	@Override
	public Controller setSessionAttr(String key, Object value) {
		// TODO Auto-generated method stub
		Map<String, Object> session = Redis.use().get(getCookie(Constants.COOKIE_NAME).toString());
		if(session == null){
			session = new HashMap<String, Object>();
		}
		session.put(key, value);
		Redis.use().setex(getCookie(Constants.COOKIE_NAME).toString(),Constants.SESSION_TIME,session);
		return this;
	}
	
}
