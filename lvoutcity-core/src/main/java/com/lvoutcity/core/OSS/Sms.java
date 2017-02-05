package com.lvoutcity.core.OSS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class Sms {

	static String name;
	static String url;
	static String pwd;
	static String template_rpwd;
	static String template_pay;
	static String template_user_pwd;
	static{
		Prop p = PropKit.use("sms.props"); 
		name = p.get("name");
		url = p.get("url");
		pwd = p.get("pwd");
		template_rpwd = p.get("template_rpwd");
		template_pay = p.get("template_pay");
		template_user_pwd = p.get("template_user_pwd");
	}
	
	
	public static boolean sendRestPwdMsg(String toMobile, String newPwd) throws Exception{
		
		return sendTextMsg(toMobile, String.format(template_rpwd, newPwd),"旅程在线");
	
	}
	
	public static boolean sendPayComplete(String toMobile, String routeName, String orderNo) throws Exception {

		return sendTextMsg(toMobile, String.format(template_pay, routeName, orderNo), "旅程在线");
	
	}
	
	public static boolean sendUserComplete(String toMobile, String pwd) throws Exception {
		System.out.println(String.format(template_user_pwd,toMobile, pwd));
		return sendTextMsg(toMobile, String.format(template_user_pwd,toMobile, pwd), "旅程在线");
	
	}
	
	public static boolean sendTextMsg(String toMobile, String content) throws Exception{
		return sendTextMsg(toMobile, content,"旅程在线");
	}
	
	public static boolean sendTextMsg(String toMobile, String content, String sign) throws Exception{
		StringBuffer sb = new StringBuffer(url);
		sb.append("name=").append(name);
		sb.append("&pwd=").append(pwd);
		sb.append("&mobile=").append(toMobile);
		// 向StringBuffer追加消息内容转URL标准码
		System.out.println(URLEncoder.encode(content,"UTF-8"));
		sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
		//加签名
		sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		//type为固定值pt  extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		System.out.println();
		// 创建url对象
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));

		// 返回发送结果
		String inputline = in.readLine();
		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
		System.out.println(inputline);

		String[] response = inputline.split(",");
		
		return response[0].equals("0");
	}
	
	
	
	public static void main(String[] args) throws Exception{
		sendRestPwdMsg("13851436019","测试");		
	}
}
