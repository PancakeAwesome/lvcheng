package com.lvoutcity.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.core.exception.BaseException;

/**
 * 
 * @ClassName: LvoutcityUtils
 * @Description: 旅游项目帮助类
 * @author James_w.s
 * @date 2016年5月10日 下午12:46:38
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LvoutcityUtils {

	private static final Log log = LogFactory.getLog(LvoutcityUtils.class);

	/**
	 * @return
	 * 
	 * @Title: getUUID @Description: TODO(这里用一句话描述这个方法的作用) @param 设定文件 @return
	 * void 返回类型 @datetiem 2016下午1:35:24 @throws
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 
	 * @Title: encryptPassword @Description: MD5加密 @param @param
	 * password @param @return 设定文件 @return String 返回类型 @datetiem
	 * 2016下午2:15:00 @throws
	 */
	public static String encryptPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			log.info("异常" + e);
			throw new BaseException(e);
		}
	}

	/**
	 * 
	 * @Title: isNull @Description: 非空判断 @param @param obj @param @return
	 * 设定文件 @return boolean 返回类型 @datetiem 2016下午2:16:20 @throws
	 */
	public static boolean isNull(Object obj) {
		return obj == null || "".equals(obj.toString().trim()) || "null".equals(obj.toString().trim())
				|| "undefined".equals(obj.toString().trim());
	}

	/**
	 * 
	 * @Title: isNotNull @Description: 非空判断 @param @return 设定文件 @return boolean
	 * 返回类型 @datetiem 2016下午2:16:52 @throws
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * 
	 * @Title: isNull @Description: 判断一个至少为空 @param @param objs @param @return
	 * 设定文件 @return boolean 返回类型 @datetiem 2016下午2:18:22 @throws
	 */
	public static boolean isNull(Object... objs) {
		if (objs == null) {
			return true;
		}
		for (Object obj : objs) {
			if (isNull(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: isAllNotNull @Description: 判断都不为空 @param @param
	 * objs @param @return 设定文件 @return boolean 返回类型 @create_time
	 * 2016下午1:45:48 @create_user James w.s @throws
	 */
	public static boolean isAllNotNull(Object... objs) {
		if (isNull(objs)) {
			return false;
		}
		for (Object obj : objs) {
			if (isNull(obj)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @Title: getPage @Description: 连接查询数据转换 @param @param list @param @return
	 * 设定文件 @return Page<T> 返回类型 @user James w.s @create_time
	 * 2016下午2:43:24 @throws
	 */
	public static <T> Page<T> getPage(Page<T> list) {
		if (isNull(list)) {
			return null;
		}
		List<Map<String, Object>> pag = new ArrayList<Map<String, Object>>();
		List<Record> map = (List<Record>) list.getList();
		for (Record json : map) {
			System.out.println(json.getColumns());
			pag.add(json.getColumns());
		}

		return new Page(pag, list.getPageNumber(), list.getPageSize(), list.getTotalPage(), list.getTotalRow());
	}

	/**
	 * 
	 * @Title: getList @Description: Record 处理 @param @param list @param @return
	 * 设定文件 @return List<T> 返回类型 @create_time 2016上午10:21:12 @create_user James
	 * w.s @throws
	 */
	public static <T> List<T> getList(List<T> list) {
		if (isNull(list)) {
			return null;
		}
		List<Map<String, Object>> pag = new ArrayList<Map<String, Object>>();
		List<Record> map = (List<Record>) list;
		for (Record json : map) {
			pag.add(json.getColumns());
		}

		return (List<T>) pag;
	}

	/**
	 * 
	 * @Title: getMapImg @Description: 地图图片 @param @param str @param @return
	 * 设定文件 @return String 返回类型 @create_time 2016下午3:01:28 @create_user James
	 * w.s @throws
	 */
	public static String getMapImg(String str) {

		switch (str) {
		case "1":
			return Constants.MAP_RENDEZVOUS_IMG;
		case "2":
			return Constants.MAP_STATR_IMG;
		case "3":
			return Constants.MAP_END_IMG;
		case "4":
			return Constants.MAP_TIME_IMG;
		case "5":
			return Constants.MAP_ATTR_IMG;
		}
		return null;
	}

	/**
	 * 
	 * @Title: parseToListSplit @Description: 字符串转换成List<String> @param @param
	 * str @param @param split @param @return 设定文件 @return List<String>
	 * 返回类型 @create_time 2016下午8:23:10 @create_user James w.s @throws
	 */
	public static List<String> parseToListSplit(String str, String split) {
		List<String> list = new ArrayList<String>();
		if (isNotNull(str)) {
			StringTokenizer st = new StringTokenizer(str, "[" + split.trim() + "]");
			while (st.hasMoreTokens()) {
				list.add(st.nextToken());
			}
		}
		return list;
	}

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestMysign(Map<String, String> sPara) {
		String prestr = LvcpayUtils.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		if (Constants.SIGN_TYPE.equals("MD5")) {
			mysign = MD5.sign(prestr, Constants.KEY, Constants.CHAT_SET);
		}
		return mysign;
	}

	public static String formateDate(String formate) {
		SimpleDateFormat time = new SimpleDateFormat(formate);
		return time.format(new Date());
	}

	/**
	 * 
	 * @Title: parseToMap @Description: 转map @param @param
	 * javaBean @param @return 设定文件 @return Map<String,String> 返回类型 @create_time
	 * 2016下午4:18:40 @create_user James w.s @throws
	 */
	public static Map<String, String> parseToMap(Map<String, String[]> str) {
		Map<String, String> map = new HashMap<String, String>();
		Set<Entry<String, String[]>> entrys = str.entrySet();
		for (Entry<String, String[]> entry : entrys) {
			map.put(entry.getKey(), entry.getValue()[0]);
		}
		// map.put( "activityId", "ac_001" );
		// request.setAttribute( "param", map );
		// request.setAttribute( "pageParam", JSONObject.fromObject( map ) );
		return map;
	}

	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value.toString());
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}

	public static Map<String, String> parseBeanToMap(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, LvoutcityUtils.isNotNull(value) ? value.toString() : null);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}

	public static Integer randomInt() {
		return (int) (Math.random() * 9000) + 1000;
	}

	public static String getAppUrl() {
		Prop prop = PropKit.use("config.props");
		String url = "http://localhost:8080";
		if (!LvoutcityUtils.isNull(prop.get("app.url"))) {
			url = prop.get("app.url");
		}
		return url;
	}
	 
	public static String getHttpUrl() {
		Prop prop = PropKit.use("config.props");
		String url = "http://localhost:8080";
		if (!LvoutcityUtils.isNull(prop.get("http.url"))) {
			url = prop.get("http.url");
		}
		return url;
	}
	/**
	 * @param type 1:只包含数字 2:数字和小写字母 3：数字和大小写字母
	 * @param length 需要的长度
	 * @return
	 */
	public static String getRandomString(int type, int length){
		String base = null;     
		switch(type){
		case 1: base = "0123456789"; break;
		case 2: base = "abcdefghijklmnopqrstuvwxyz0123456789"; break;
		case 3: base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; break;
		default: base = "0123456789";     
		}
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }
	    return sb.toString();
	}
}
