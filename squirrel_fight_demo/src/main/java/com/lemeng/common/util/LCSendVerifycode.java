package com.lemeng.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.PropertyResourceBundle;

import static com.sun.org.apache.xml.internal.resolver.Catalog.URI;

/**
 * @author hanhuaiwei
 */
public class LCSendVerifycode {
	private static Logger logger = LoggerFactory.getLogger(LCSendVerifycode.class);
	private static String url;
	private static String username;
	private static String passwd;
	private static HttpClient httpclient = new HttpClient();
	static {
		try {
			InputStream fis = LCSendVerifycode.class.getClassLoader()
					.getResourceAsStream("sendsms.properties");
			PropertyResourceBundle props = new PropertyResourceBundle(fis);
			url = props.getString("url");
			username = props.getString("username");
			passwd = props.getString("passwd");
			fis.close();
			httpclient.getParams().setIntParameter(
					CoreConnectionPNames.SO_TIMEOUT, 15000);
			httpclient.getParams().setIntParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			httpclient.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 *            应用地址，类似于http://ip:port/msg/
	 * @param account
	 *            账号
	 * @param pswd
	 *            密码
	 * @param mobile
	 *            手机号码，多个号码使用","分割
	 * @param msg
	 *            短信内容
	 * @param needstatus
	 *            是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 */
	public static boolean batchSendVerifycode(String mobile, String msg) throws Exception {
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[] {
					new NameValuePair("account", username),
					new NameValuePair("pswd", passwd),
					new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(true)),
					new NameValuePair("msg", msg),
					new NameValuePair("product", ""),
					new NameValuePair("extno", ""), });
			int result = httpclient.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				System.out.println("post请求的访问地址："+method.getURI());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				String respresult = URLDecoder.decode(baos.toString(), "UTF-8");
				System.out.println(respresult);
				logger.info("发送验证码返回：" + respresult);
				if(!StringUtils.isEmpty(respresult)&&respresult.contains(",")){
					return respresult.split(",")[1].startsWith("0") ? true:false;
				}else{
					return false;
				}
			} else {
				logger.error("HTTP ERROR Status: "+ method.getStatusCode() + ":" + method.getStatusText());
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	public static void main(String[] args) throws Exception {
		boolean flag = batchSendVerifycode("17600971251", "本次验证码为：123456.请在页面输入完成验证。如非本人操作，请忽略！[赏吧]");
		System.out.println(flag);
	}
}
