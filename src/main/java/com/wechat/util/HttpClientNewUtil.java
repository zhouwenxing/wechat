package com.wechat.util;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("deprecation")
public class HttpClientNewUtil {

	@SuppressWarnings("rawtypes")
	public static String post(String url, Map paramMap) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		try {
			RequestBuilder reguestBuilder = RequestBuilder.post().setUri(
					new URI(url));
			Iterator iter = paramMap.entrySet().iterator();// 将参数添加到POST的参数中
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();// 创建参数队列
			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				String key = String.valueOf(entry.getKey());
				String val = String.valueOf(entry.getValue());
				if (val.contains("{")) {
					val = val.replace("\"", "");
				}
				formparams.add(new BasicNameValuePair(key, val));
			}
			reguestBuilder.setEntity(new UrlEncodedFormEntity(formparams,
					Consts.UTF_8)); // 防止中文乱码
			HttpUriRequest httpUriRequest = reguestBuilder.build();
			CloseableHttpResponse response2 = httpclient
					.execute(httpUriRequest);
			try {
				HttpEntity entity = response2.getEntity();
				String retStr = EntityUtils.toString(entity, "utf-8");
				return retStr;

			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}
	}

	@SuppressWarnings("resource")
	public static String httpPostWithJson(JSONObject jsonObj, String url) {
		HttpPost post = null;
		HttpClient httpClient = new DefaultHttpClient();

		post = new HttpPost(url);
		// 构造消息头
		post.setHeader("Content-type", "application/json; charset=utf-8");
		post.setHeader("Connection", "Close");
		// 构建消息实体
		StringEntity entity = new StringEntity(jsonObj.toString(),
				Charset.forName("UTF-8"));
		entity.setContentEncoding("UTF-8");
		// 发送Json格式的数据请求
		entity.setContentType("application/json");
		post.setEntity(entity);

		HttpResponse response;
		String responseString = "";
		try {
			response = httpClient.execute(post);
			HttpEntity responseEntity = response.getEntity();
			responseString = EntityUtils.toString(responseEntity, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
		}
		return responseString;
	}
}
