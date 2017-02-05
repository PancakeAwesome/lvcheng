
package com.lvoutcity.core.pay.alipay.sign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.net.URLEncoder;

import javax.crypto.Cipher;

public class RSA{
	
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	
	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String input_charset)
	{
        try 
        {
        	PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) ); 
        	KeyFactory keyf 				= KeyFactory.getInstance("RSA");
        	PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );

            byte[] signed = signature.sign();
            
            return Base64.encode(signed);
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        
        return null;
    }
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key 支付宝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decode(ali_public_key);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		
			java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS);
		
			signature.initVerify(pubKey);
			signature.update( content.getBytes(input_charset) );
		
			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	* 解密
	* @param content 密文
	* @param private_key 商户私钥
	* @param input_charset 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;
		
		keyBytes = Base64.decode(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}
	
	
	public static  void main(String[] args){
		try {
			System.out.println(URLEncoder.encode((RSA.sign("partner=\"2088221758502079\"&seller_id=\"postmaster@lvoutcity.com\"&out_trade_no=\"100000000160\"&subject=\"订单支付\"&body=\"订单支付\"&total_fee=\"1.00\"&notify_url=\"http://www.xxx.com\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&show_url=\"m.alipay.com\"", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALRQ8bvQjCN+ubxEdytyKNClApCDq+o6Bwp50eb/qKOHuutOZ/bRP4jfxvfylfgkpr56K5les+zI+nvtvvNAWTUYuqjhXj4Z5WzVGbLebPBLU97deZlU4EGhgggMiG1d3yWmxR9vov8hl1++nL4MfI4RNYYp6d1dc/xvYtlTtBvLAgMBAAECgYEAjphQgnlqyYsm8Nan8yilVFKZ05T9OA7JkKRDtUzKnP/ydrEbkFlryB9HZr9lj8XUQ1+z6Oa2nPNW+vhJgG6L3BhzL+7osvK3mL90nRoh/rG/UMyswmruJIvopZjFEiZX6VcQ+FpMmYjfvuSz70xbnFyvMj2Cf/QUShIHKLFPVbECQQDfEYgxGYD9UBKHRW0VqFFQzITianDfjlLIzH6/z5Bn38mNto2qPghuRpK3A3DbKvjqacliCqFMtcwdHJkLHOUzAkEAzu+qvM1HAYMfqC5RLlLrVbjO389jdRNDWLIhaOxlvmmy+BqZmU87C8454SQCBtRXvLZQrROSRtlgBUqAHpW/CQJAcpNQvtd4t6PvQGfales0iSnjf2BoxYsW3mibGe6FH7WEkzOXJEBkmC/LoBE2YHx8Cft4ItGO9BXU14ce3ORknQJBAMnGCzb4+clRr2Zvcxy2IV1+S8kTEKkxVTOAzO7fwf0+VxFv7mD8TliehvUuBQPfvIT3SAba1IBcqDHAiNhJrvECQAK7Hcpc6+CAfz8qylbcIIm5gXZfDKz76wg+LDZ9FBoPAAaHq2I0xrS5inTT3bkyBlYQDAnKr4VbO70+IXi09I0=", "utf-8")),"utf-8"));
			System.out.println((RSA.sign(URLEncoder.encode("partner=\"2088221758502079\"&seller_id=\"postmaster@lvoutcity.com\"&out_trade_no=\"100000000160\"&subject=\"订单支付\"&body=\"订单支付\"&total_fee=\"1.00\"&notify_url=\"http://www.xxx.com\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&show_url=\"m.alipay.com\"", "utf-8"),"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALRQ8bvQjCN+ubxEdytyKNClApCDq+o6Bwp50eb/qKOHuutOZ/bRP4jfxvfylfgkpr56K5les+zI+nvtvvNAWTUYuqjhXj4Z5WzVGbLebPBLU97deZlU4EGhgggMiG1d3yWmxR9vov8hl1++nL4MfI4RNYYp6d1dc/xvYtlTtBvLAgMBAAECgYEAjphQgnlqyYsm8Nan8yilVFKZ05T9OA7JkKRDtUzKnP/ydrEbkFlryB9HZr9lj8XUQ1+z6Oa2nPNW+vhJgG6L3BhzL+7osvK3mL90nRoh/rG/UMyswmruJIvopZjFEiZX6VcQ+FpMmYjfvuSz70xbnFyvMj2Cf/QUShIHKLFPVbECQQDfEYgxGYD9UBKHRW0VqFFQzITianDfjlLIzH6/z5Bn38mNto2qPghuRpK3A3DbKvjqacliCqFMtcwdHJkLHOUzAkEAzu+qvM1HAYMfqC5RLlLrVbjO389jdRNDWLIhaOxlvmmy+BqZmU87C8454SQCBtRXvLZQrROSRtlgBUqAHpW/CQJAcpNQvtd4t6PvQGfales0iSnjf2BoxYsW3mibGe6FH7WEkzOXJEBkmC/LoBE2YHx8Cft4ItGO9BXU14ce3ORknQJBAMnGCzb4+clRr2Zvcxy2IV1+S8kTEKkxVTOAzO7fwf0+VxFv7mD8TliehvUuBQPfvIT3SAba1IBcqDHAiNhJrvECQAK7Hcpc6+CAfz8qylbcIIm5gXZfDKz76wg+LDZ9FBoPAAaHq2I0xrS5inTT3bkyBlYQDAnKr4VbO70+IXi09I0=", "utf-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
