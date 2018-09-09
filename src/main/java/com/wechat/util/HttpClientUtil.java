package com.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


@SuppressWarnings("deprecation")
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 360000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 360000;

	public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=utf-8";

	public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=utf-8";
	
	public static final String CONTENT_TYPE_FORM_CHARSET = "application/x-www-form-urlencoded;charset=utf-8";

	/**
	 * httpclient读取内容时使用的字符集
	 */
	public static final String CONTENT_CHARSET = "UTF-8";

	public static final Charset UTF_8 = Charset.forName("UTF-8");

	public static final Charset GBK = Charset.forName(CONTENT_CHARSET);
	
	// 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
		private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
			// 自定义的恢复策略
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				// 设置恢复策略，在发生异常时候将自动重试3次
				if (executionCount >= 3) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// Do not retry on SSL handshake exception
					return false;
				}
				HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
				if (!idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}
		};

	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String sendGetRequest(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		return sendGetRequest(url, params,CONTENT_CHARSET);
	}
	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String sendGetRequest(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {

		CloseableHttpClient client = buildHttpClient(true);

		HttpGet get = buildHttpGet(url, params, charset);
		
		return execute(client, get, charset);
	}

	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostRequest(String url, Map<String, String> params)
			throws URISyntaxException, ClientProtocolException, IOException {
		return sendPostRequest(url, params,CONTENT_CHARSET);
	}
	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostRequest(String url, Map<String, String> params,String charset)
			throws ClientProtocolException, IOException, URISyntaxException {

		CloseableHttpClient client = buildHttpClient(true);

		HttpPost post = buildHttpPost(url, params, charset);
		
		return execute(client, post, charset);

	}
	
	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostRequest(String url, String params)
			throws URISyntaxException, ClientProtocolException, IOException {
		return sendPostRequest(url, params,CONTENT_CHARSET);
	}
	/**
	 * 简单post调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostRequest(String url, String params,String charset)
			throws ClientProtocolException, IOException, URISyntaxException {

		CloseableHttpClient client = buildHttpClient(true);

		HttpPost post = buildHttpPost(url, params, charset);
		
		return execute(client, post, charset);

	}
	
	
	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String sendSSLGetRequest(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLGetRequest(url, params,CONTENT_CHARSET,null,null);
	}
	
	/**
	 * https请求
	 * @param url
	 * @param params 参数
	 * @param keyStorePath 密钥路径
	 * @param password 密钥密码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLGetRequest(String url, Map<String, String> params, String keyStorePath,String password)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLGetRequest(url,params,CONTENT_CHARSET,keyStorePath,password);
	}
	/**
	 * 简单get调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String sendSSLGetRequest(String url, Map<String, String> params, String charset, 
			String keyStorePath,String password) 
					throws ClientProtocolException, IOException, URISyntaxException, CertificateException {

		CloseableHttpClient client = buildHttpsClient(keyStorePath,password);

		HttpGet get = buildHttpGet(url, params, charset);
		
		return execute(client, get, charset);
	}
	
	/**
	 * 带证书的https请求
	 * @param url 
	 * @param params 参数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLPostRequest(url,params,CONTENT_CHARSET,null,null);
	}
	
	/**
	 * https请求
	 * @param url
	 * @param params 参数
	 * @param keyStorePath 密钥路径
	 * @param password 密钥密码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, Map<String, String> params, String keyStorePath,String password)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLPostRequest(url,params,CONTENT_CHARSET,keyStorePath,password);
	}
	
	/**
	 * https请求
	 * @param url
	 * @param params 参数
	 * @param charset 编码类型
	 * @param keyStorePath 密钥路径
	 * @param password 密钥密码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, Map<String, String> params, String charset, 
															String keyStorePath,String password) 
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {

		CloseableHttpClient client = buildHttpsClient(keyStorePath,password);

		HttpPost post = buildHttpPost(url, params, charset);
		
		return execute(client, post, charset);

	}
	
	
	/**
	 * 带证书的https请求
	 * @param url 
	 * @param params 参数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, String params)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLPostRequest(url,params,CONTENT_CHARSET,null,null);
	}
	
	/**
	 * https请求
	 * @param url
	 * @param params 参数
	 * @param keyStorePath 密钥路径
	 * @param password 密钥密码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, String params, String keyStorePath,String password)
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {
		return sendSSLPostRequest(url,params,CONTENT_CHARSET,keyStorePath,password);
	}
	
	/**
	 * https请求
	 * @param url
	 * @param params 参数
	 * @param charset 编码类型
	 * @param keyStorePath 密钥路径
	 * @param password 密钥密码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CertificateException
	 */
	public static String sendSSLPostRequest(String url, String params, String charset, String keyStorePath,String password) 
			throws ClientProtocolException, IOException, URISyntaxException, CertificateException {

		CloseableHttpClient client = buildHttpsClient(keyStorePath,password);

		HttpPost post = buildHttpPost(url, params, charset);
		
		return execute(client, post, charset);

	}
	
	public static Map<String,String> string2Map(String data){
		Map<String,String> map = new HashMap<String,String>();
		String[] array = data.split("\\&");
		for(String str : array){
			String[] params = str.split("=");
			map.put(params[0], params[1]);
		}
		return map;
	}
	

	/**
	 * 创建HttpClient
	 * 
	 * @param isMultiThread
	 * @return
	 */
	private static CloseableHttpClient buildHttpClient(boolean isMultiThread) {

		CloseableHttpClient client;

		if (isMultiThread){
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE);
			client = HttpClientBuilder
					.create().setRetryHandler(requestRetryHandler)
					.setConnectionManager(new PoolingHttpClientConnectionManager(registryBuilder.build()))
					.setDefaultRequestConfig(buildRequestConfig()).build();
		}else{
			client = HttpClientBuilder.create().setRetryHandler(requestRetryHandler)
					.setDefaultRequestConfig(buildRequestConfig()).build();
		}
		return client;
	}
	
	/**
	 * 创建HttpsClient
	 * 
	 * @param isMultiThread
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws CertificateException 
	 */
	private static CloseableHttpClient buildHttpsClient(String keyStorePath,String password) throws CertificateException, FileNotFoundException, IOException {

		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE);
		//指定信任密钥存储对象和连接套接字工厂  
		try {  
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			if(StringUtils.isNotEmpty(keyStorePath)){
				trustStore.load(new FileInputStream(new File(keyStorePath)), password.toCharArray());
			}
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStore, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                    return true;
                }
            }).build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
			registryBuilder.register("https", sslSF);  
		} catch (KeyStoreException e) {
			logger.error("创建SSLSocketFactory异常 KeyStoreException", e);
		    throw new RuntimeException(e);  
		} catch (KeyManagementException e) {
			logger.error("创建SSLSocketFactory异常 KeyManagementException", e);
		    throw new RuntimeException(e);  
		} catch (NoSuchAlgorithmException e) {
			logger.error("创建SSLSocketFactory异常 NoSuchAlgorithmException", e);
			throw new RuntimeException(e);  
		} catch (Exception e){
			logger.error("创建SSLSocketFactory异常 Exception", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        }
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		//设置连接管理器  
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);  
		//构建客户端  
		return HttpClientBuilder.create().setConnectionManager(connManager)
				.setRetryHandler(requestRetryHandler)
				.setDefaultRequestConfig(buildRequestConfig()).build();
	}
	

	/**
	 * 构建httpPost对象
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	private static HttpPost buildHttpPost(String url, Map<String, String> params, String charset)
			throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post,charset); //设置HttpMethod通用配置
		HttpEntity he = null;
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}
			he = new UrlEncodedFormEntity(formparams, UTF_8);
			post.setEntity(he);
		}
		// 在RequestContent.process中会自动写入消息体的长度，自己不用写入，写入反而检测报错
		// setContentLength(post, he);
		return post;
	}
	
	
	/**
	 * 构建httpPost对象
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	private static HttpPost buildHttpPost(String url, String params, String charset)
			throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post,charset); //设置HttpMethod通用配置
		if (params != null) {
			StringEntity s = new StringEntity(params.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
		}
		// 在RequestContent.process中会自动写入消息体的长度，自己不用写入，写入反而检测报错
		// setContentLength(post, he);
		return post;
	}

	/**
	 * 构建httpGet对象
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws URISyntaxException
	 */
	private static HttpGet buildHttpGet(String url, Map<String, String> params, String charset)
			throws URISyntaxException {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		StringBuffer uriStr = new StringBuffer((url.indexOf("?")) < 0 ? url : url
				.substring(0, url.indexOf("?") ) );//判断url是否含 ?
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				ps.add(new BasicNameValuePair(key, params.get(key)));
			}
			uriStr.append("?");
			uriStr.append(URLEncodedUtils.format(ps, UTF_8));
		}
		HttpGet get = new HttpGet(uriStr.toString());
		setCommonHttpMethod(get,charset); //设置HttpMethod通用配置
		return get;
	}
	
	private static String execute(CloseableHttpClient client, HttpUriRequest httpUriRequest, String charset)
			throws ClientProtocolException, IOException, URISyntaxException {
		try {
			HttpResponse response = client.execute(httpUriRequest);

			assertStatus(response);
			
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String returnStr = EntityUtils.toString(entity, charset);
				EntityUtils.consume(entity);
				return returnStr;
			}
		} catch (ClientProtocolException e) {
			logger.error("客户端连接协议错误",e);
			throw new ClientProtocolException("客户端连接协议错误", e);
		} catch (IOException e) {
			logger.error("IO操作异常",e);
			throw new IOException("IO操作异常", e);
		} catch (Exception e){
			logger.error("发送请求异常 Exception", e);
            throw new RuntimeException("创建SSLSocketFactory异常", e);
        } finally {
			if (httpUriRequest != null) {
				httpUriRequest.abort();
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					logger.error("关闭httpclient失败",e);
				}
			}
		}
		return null;
	}

	/**
	 * 设置HttpMethod通用配置
	 * 
	 * @param httpMethod
	 */
	private static void setCommonHttpMethod(HttpRequestBase httpMethod, String charset) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, charset);// setting
		httpMethod.addHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");	// contextCoding
		httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
		httpMethod.setHeader("referer","http://reg.renren.com/xn6218.do?ss=10131&rt=1&f=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D9YR170RpGrSRij7tmq3CRdOJcwB0okUa-_Xpz3ZEgBK%26wd%3D%26eqid%3Dad1960cd000146c5000000065a12d54b&g=v6reg");
		// httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_XML_CHARSET);
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 * 
	 * @param httpMethod
	 * @param he
	 */
	@SuppressWarnings("unused")
	private static void setContentLength(HttpRequestBase httpMethod,
			HttpEntity he) {
		if (he == null) {
			return;
		}
		httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
	}

	/**
	 * 构建公用RequestConfig
	 * 
	 * @return
	 */
	private static RequestConfig buildRequestConfig() {
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(SO_TIMEOUT_MS)
				.setConnectTimeout(CONNECTION_TIMEOUT_MS)
				.setCookieSpec(CookieSpecs.STANDARD).build();
		return requestConfig;
	}

	/**
	 * 强验证必须是200状态否则报异常
	 * @param res
	 * @throws HttpException
	 */
	private static void assertStatus(HttpResponse res) throws IOException{
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		switch (res.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:
//		case HttpStatus.SC_CREATED:
//		case HttpStatus.SC_ACCEPTED:
//		case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
//		case HttpStatus.SC_NO_CONTENT:
//		case HttpStatus.SC_RESET_CONTENT:
//		case HttpStatus.SC_PARTIAL_CONTENT:
//		case HttpStatus.SC_MULTI_STATUS:
			break;
		default:
			throw new IOException("服务器响应状态异常,失败");
		}
	}
	
	
	
}
