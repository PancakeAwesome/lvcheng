package com.lvoutcity.core.api;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.api.Restful.Method;

/**
 * API Base Controller
 * 
 * @author gdh
 *
 */
public class BaseApiController extends Controller {

	@Override
	public <T> T getBean(Class<T> beanClass) {
		if (getRequest().getMethod().toLowerCase().equals(Method.Post.name().toLowerCase())) {

			System.out.println("getBean :"+getAttrForStr(Constant.BODY_KEY));
			T t = JSON.parseObject(getAttrForStr(Constant.BODY_KEY), beanClass);
			if (t != null)
				return t;
		}

		return getBean(beanClass, "");
	}
	
	public <T> T getBeanOrigin(Class<T> beanClass) {
		return getBean(beanClass, "");
	}
	

	
	public TokenBean getTokenBean(){
		return getAttr(Constant.TokenBean);
	}
	
	public List<Object> renderDatatableFromDb(List<?> list) {
		List<Object> list_json = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Object ob = JSON.parse(((Record) list.get(i)).toJson());
			list_json.add(ob);
		}
		return  list_json;
	}
}
