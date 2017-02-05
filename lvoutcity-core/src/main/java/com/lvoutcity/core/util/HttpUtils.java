package com.lvoutcity.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * 请求数据http URL
 * <功能详细描述>
 * 
 * @author  Administrator
 * @version  [版本号, 2015年1月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressWarnings({"deprecation","resource"})
public class HttpUtils {

	protected static final Log log = LogFactory.getLog(HttpUtils.class);
	/**
	 * 
	 * @Title: getHttpURLPost
	 * @Description: http client 请求 POST 
	 * @return String 返回类型
	 * @date 2015年2月3日下午3:30:16
	 * @throws @param url
	 * @throws @param param
	 * @throws @param charSet 字符集编码 默认为 UTF-8
	 * @throws @return
	 */

	public static String getHttpURLPost(String url,List<NameValuePair> param,String charSet){
		log.info("http-----进入post http 请求");
		HttpClient client = new DefaultHttpClient();
		
		String result = "";
		
		HttpPost post = new HttpPost(url);
		log.info("http-----进入post http 请求 url-------->"+post);
		try {
			if(!param.isEmpty()){
				post.setEntity(new UrlEncodedFormEntity(param,"UTF-8"));
			}
			HttpResponse response = client.execute(post);
			
			//返回结果
			HttpEntity  entity = response.getEntity();
			log.info("http-----进入post http 请求 信息-------->"+entity);
			if(entity != null){
				
				entity = new BufferedHttpEntity(entity);
				
				InputStream in = entity.getContent();
				
				byte [] read = new byte[1024];
				byte [] all = new byte[0];
				/*result = ips2String(in);*/
				
				int num;
				while((num = in.read(read))>0){
					byte [] temp = new byte[all.length + num];
					System.arraycopy(all, 0, temp, 0, all.length);
					System.arraycopy(read, 0, temp, all.length, num);
					all = temp;
				}
				
				result = new String(all, LvoutcityUtils.isNotNull(charSet) ? charSet : "UTF-8");
				log.info("http-----进入post http 请求 结果-------->"+result);
				if(null != in){
					in.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("http-----进入post http 结束-------->"+e.getMessage()+" error: "+e);
		}
		log.info("http-----进入post http 结束-------->");
		post.abort();
		return result;
		
	}

	
	/**
	 * 
	 * @Title: getHttpURLGet 
	 * @Description: GET 请求 http client
	 * @param @param url
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @datetime 2015年7月30日 上午10:13:39
	 * @createuser ws
	 * @throws
	 */
	public static String getHttpURLGet(String url){
		//创建默认的 HttpClient 实例  
		HttpClient httpClient = new DefaultHttpClient();  
        String content ="";
        try {  
            //创建 httpUriRequest 实例  
            HttpGet httpGet = new HttpGet(url);  
            System.out.println("uri=" + httpGet.getURI());  
  
            //执行 get 请求  
            HttpResponse httpResponse = httpClient.execute(httpGet);  
  
            //获取响应实体  
            HttpEntity httpEntity = httpResponse.getEntity();  
            //打印响应状态  
            System.out.println(httpResponse.getStatusLine());  
            if (httpEntity != null) {  
                //响应内容的长度  
                long length = httpEntity.getContentLength();  
                //响应内容  
                content = EntityUtils.toString(httpEntity);  
  
                System.out.println("Response content length:" + length);  
                System.out.println("Response content:" + content);  
            }  
  
            //有些教程里没有下面这行  
            httpGet.abort();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            //关闭连接，释放资源  
            httpClient.getConnectionManager().shutdown();  
        }  
        return content;
	}
	
    /**
     * 获取soap请求头，并替换其中的标志符号为用户的输入符号
     * @param city 用户输入城市名
     * @return 用户将要发送给服务器的soap请求
     */
    private static String getSoapRequest(String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                        + "<soap:Body>   "
                        + " <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">"
                        	+ "<theCityName>" + city + "</theCityName>   "
                        + " </getWeatherbyCityName>"
                        + "</soap:Body></soap:Envelope>");
        return sb.toString();
    }
    
    /**
     * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
     * @param city 用户输入的城市名称
     * @return 服务器端返回的输入流，供客户端读取
     * @throws Exception
     */
    public static InputStream getSoapInputStream(String city) throws Exception {
        try {
            String soap = getSoapRequest(city);
            if (soap == null) {
                return null;
            }
            URL url = new URL(
                    "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", Integer.toString(soap
                    .length()));
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction",
                    "http://WebXml.com.cn/getWeatherbyCityName");
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
            osw.write(soap);
            osw.flush();
            osw.close();
            InputStream is = conn.getInputStream();
            //System.out.println(is.toString());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
    /**
     * 通过dom4j对服务器端返回的XML进行解析
     * @param city 用户输入的城市名称
     * @return 符串 用,分割
     */
    public static String getWeather(String city) {
//        Document document=null;
//        SAXReader reader = new SAXReader();
//        String s="";
//        Map<String,String> map=new HashMap<String,String>();
//        map.put("design", "http://WebXml.com.cn/");
//        reader.getDocumentFactory().setXPathNamespaceURIs(map);
//        try {
//            InputStream is = getSoapInputStream(city);//得到输入流
//            document=reader.read(is);//将输入流转化为document
//            String t = document.asXML();
//            System.out.println(t);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        List<?> nodes = document.selectNodes("//design:string");
//        for (Iterator<?> it = nodes.iterator(); it.hasNext();) {
//            Element elm = (Element) it.next();
////            String text=elm.getText();
//            //System.out.println("fsffs"+text);
//            s=s+elm.getText()+"\n";
//        }
//        return s;
    	return null;
    }
	
    
    
	public static void main(String[] args) {
//		List<NameValuePair> param = new ArrayList<NameValuePair>();
//		param.add(new BasicNameValuePair("mobile","15062020358"));
//		param.add(new BasicNameValuePair("templateId","eSOFfF1aQMeaDL9IPaaFTv"));
//		param.add(new BasicNameValuePair("region","86"));
//		//param.add(new BasicNameValuePair("name","附件5：2015年度市局绩效考评加减分项目明细表.xls"));
//		String uril = getHttpURLPost("http://api.sms.ronghub.com/sendCode.[json]", param, null);
//		System.out.println(uril);
		String d = HttpUtils.getHttpURLGet("http://restapi.amap.com/v3/geocode/regeo?key=9e039affcbfc4476e0315ff422a832fb&location=118.940104,32.004868&radius=1000&extensions=base&batch=false&roadlevel=1");
		JSONObject ad = JSONObject.parseObject(d);
//		JSONObject ad2 = JSONObject.parseObject(ad.get("regeocode").toString());
//		System.out.println(ad2);
//		JSONObject ad3 = JSONObject.parseObject(ad2.get("addressComponent").toString());
		System.out.println(ad);
//		JSONObject ad4 = JSONObject.parseObject(ad3.toJSONString());
//		System.out.println(ad4.get("country"));
	}
	
}
