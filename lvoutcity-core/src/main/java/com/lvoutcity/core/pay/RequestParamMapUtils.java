package com.lvoutcity.core.pay;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/** 
 * @ClassName: RequestParamMapUtils 
 * @Description: 请求参数转换
 * @author 管东海
 *  
 */
public final class RequestParamMapUtils {

	private RequestParamMapUtils() {
	}

	public static Map<String, String> parse(HttpServletRequest request) {
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, String> paramMap = new HashMap<String, String>(requestMap.size());

		Set<String> keys = requestMap.keySet();

		Iterator<String> keyLoop = keys.iterator();

		while (keyLoop.hasNext()) {
			String key = keyLoop.next();
			String value = requestMap.get(key)[0];

			paramMap.put(key, value);
		}
		return paramMap;
	}
	
	public static String parseToParamStr(Object rr) {
		Field[] fields = rr.getClass().getDeclaredFields();

		StringBuffer buffer = new StringBuffer();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;
			try {
				 value = field.get(rr);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if(value != null){
				buffer.append("&").append(name).append("=").append(value.toString().trim());
			}
		}
		
		if(buffer.length()>0)
			buffer.deleteCharAt(0);
		
		return buffer.toString();
		
	}
	
	public static Map<String, String> parseToParamMap(Object rr) {
		Field[] fields = rr.getClass().getDeclaredFields();
		Map<String, String> paramMap = new HashMap<String, String>();

		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;
			try {
				 value = field.get(rr);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if(value != null){
				paramMap.put(name, value.toString());
			}
		}
		
		
		return paramMap;
		
	}
	public static String getSortedParamStr(HashMap<String,String> params){
		StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (!key.isEmpty()&&value!=null&&!value.isEmpty()&&!key.equalsIgnoreCase("sign")) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        System.out.println("Sorted param str is :"+content.toString());
        return content.toString();
        
	}
}
