package javax.core.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtils {
	
	private HttpClientUtils(){}

	/**
	 * 获取HttpClient 对象实例
	 * 
	 * @param host
	 *            请求地址 www.chaoxing.com
	 * @param port
	 *            端口号 80
	 * @param protocol
	 *            协议 http
	 * @param connectionTimeout
	 *            连接超时时间
	 * @param soTimeout
	 *            获取返回结果超时时间
	 * @return HttpClient 实例
	 */
	public static HttpClient getHttpClient(String host, int port,
			String protocol,int connectionTimeout,int soTimeout) {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, protocol);
		if (connectionTimeout != 0)
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(connectionTimeout);// 连接超时时间（毫秒）
		if (soTimeout != 0)
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(soTimeout);// 获取返回结果超时时间（毫秒）
		return client;
	}
	
	/**
	 * 获取GetMethod 对象
	 * 
	 * @param url
	 *            连接地址
	 * @return GetMethod 对象
	 */
	public static GetMethod getGetMethod(String url) {
		if (url == null || "".equals(url))
			return new GetMethod();
		
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(
				HttpMethodParams.SINGLE_COOKIE_HEADER, true);// 定义是否应把Cookie在一个响应头
		getMethod.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);//设置Cookie策略，解决Cookie rejected警告
		return getMethod;
	}
	
	/**
	 * 获取返回结果
	 * 
	 * @param client
	 *            HttpClinet 实例
	 * @param getMethod
	 *            GetMethod对象
	 * @param charsetName
	 *            字符编码
	 * @return 结果字符串
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String getHttpClientResponseString(HttpClient client,
			GetMethod getMethod, String charsetName) throws HttpException,IOException {
		StringBuffer resBuffer = new StringBuffer();
		int sttatusCode = client.executeMethod(getMethod);
		if (sttatusCode == HttpStatus.SC_OK) {// 执行成功
			InputStream resStream = getMethod.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resStream, charsetName));
			String resTemp = "";
			while ((resTemp = br.readLine()) != null) {
				resBuffer.append(resTemp);
			}
		}
		return resBuffer.toString();
	}
}
