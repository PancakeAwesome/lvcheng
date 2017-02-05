/**
 * 
 */
package com.lvoutcity.core.OSS;

import java.io.File;
import java.util.Date;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * @author wj
 *
 */
public class OssClient {
		public String endpoint = "oss-cn-hangzhou.aliyuncs.com";
		public String internalEndpoint = " oss-cn-hangzhou-internal.aliyuncs.com";
		public String accessKeyId = "1ZnC902Uo76vSmpR";
		public String accessKeySecret = "THcwwg71o7V3ABjxIlQ8MUMTNIlj4S";
		public String bucket="lvc866";
		public String imgProcessEndPoint;
		
		public OssClient(){
			Prop prop = PropKit.use("oss.props");
			this.endpoint = prop.get("oss.endpoint");
			this.accessKeyId = prop.get("oss.accessKeyId");
			this.accessKeySecret = prop.get("oss.accessKeySecret");
			this.bucket = prop.get("oss.bucket");
			this.imgProcessEndPoint = prop.get("oss.imgProcessEndPoint");
			this.internalEndpoint = prop.get("oss.internalEndpoint");
		}
		
		/**
		 * 
		 * @param bucket  域
		 * @param key  
		 * @param path 文件上传服务器的路径
		 * @return 图片url
		 */
		public String  uploadFile(String key,String path){
			OSSClient client = new OSSClient(internalEndpoint, accessKeyId, accessKeySecret);
			client.putObject(bucket, key, new File(path));
			Date expiration = new Date(new Date().getTime() + 3600 * 1000);
			String url =client.generatePresignedUrl(bucket, key, expiration).toString().split("\\?")[0].replace("-internal", "");
			client.shutdown();
			return url;
		}

		
		/**
		 * 获取一小时访问路径
		 * @param bucket
		 * @param key
		 * @return
		 */
		public String  getClientUrl1(String key){
			OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			Date expiration = new Date(new Date().getTime() + 3600 * 1000);
			String url =client.generatePresignedUrl(bucket, key, expiration).toString().split("\\?")[0];
			client.shutdown();
			return url;
		}
		
		/**
		 * 创建bucket
		 * @param bucket
		 */
		public void  createBucket1(String bucket){
			OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			client.createBucket(bucket);
			client.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
			client.shutdown();
		}
		
		/**
		 * 删除文件
		 * @param bucket
		 * @param key
		 */
		public void  deleteObject(String key){
			OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			client.deleteObject(bucket, key);
			client.shutdown();
		}
		
		/**
		 * 下载文件到本地
		 * @param key 文件名
		 * @param file 本地文件
		 * @param style 样式名，可以为null
		 */
		public void getObject(String key, String filename, String style){
			getObject(key, bucket, filename, style);
		}
		
		/**
		 * 下载文件到本地
		 * @param key 文件名
		 * @param bucketName 
		 * @param file 本地文件
		 * @param style 样式名，可以为null
		 */
		public void getObject(String key, String bucketName , String filename, String style){
			// 创建OSSClient实例
			OSSClient ossClient;
			if(style!=null){
				ossClient = new OSSClient(imgProcessEndPoint, accessKeyId, accessKeySecret);
				key=key+"@"+style;
			}else{
				ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}
			// 下载object到文件
			ossClient.getObject(new GetObjectRequest(bucketName, key), new File(filename));

			// 关闭client
			ossClient.shutdown();
		}
		
		public static void main(String [] args){

			OssClient c = new OssClient();
			c.getObject("00910021c8c7424c95337fc7765930ba.jpg","testdown.jpg","1e_1c_0o_0l_60h_60w_90q.src");
		}
		
}
