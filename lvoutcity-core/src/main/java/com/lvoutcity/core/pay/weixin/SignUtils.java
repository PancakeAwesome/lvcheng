package com.lvoutcity.core.pay.weixin;

import java.lang.reflect.Field;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/** 
 * @ClassName: SignUtils 
 * @Description: 微信签名
 * @author 管东海
 *  
 */
public class SignUtils {

	private SignUtils() {
	}

	public static String getSign(Object uo,String key) throws Exception{
		Field[] fields = uo.getClass().getDeclaredFields();

		StringBuffer buffer = new StringBuffer();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			if(name.equals("package_"))
				name = "package";
			Object value = null;
			try {
				 value = field.get(uo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if(value != null){
				buffer.append("&").append(name).append("=").append(value);
			}
		}
		
		if(buffer.length()>0)
			buffer.deleteCharAt(0);
		
		buffer.append("&").append("key=").append(key);
		System.out.println("params = "+buffer.toString());
		return HashKit.md5(buffer.toString()).toUpperCase();
	}
	/**
	 * 检验微信通知的签名是否正确
	 * @param content
	 * @param sign
	 * @return
	 */
	public static boolean checkSign(String content, String sign){
		Prop props = PropKit.use("wxpay.props");
		String key = props.get("wx.key");
		return checkSign(content,sign,key);
	}
	

	public static boolean checkSign(String content, String sign,String key){
		return HashKit.md5(content+"&key="+key).toUpperCase().equals(sign);
	}
}
