package com.zhixin.wolf.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public HttpResponse head(String url, Map<String, String> headerdatas) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpHead httphead = new HttpHead(url);
		for (String key : headerdatas.keySet()) {
			httphead.addHeader(key, headerdatas.get(key));
		}
		return httpclient.execute(httphead);
	}

	public HttpResponse head(String url, String XAuthToken) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		headerdatas.put("X-Auth-Token", XAuthToken);
		return this.head(url, headerdatas);
	}

	public static HttpResponse get(String url, Map<String, String> headerdatas, Map<String, String> params)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		if (params.size() > 0) {
			url += "?";
			int flag = 0;
			for (String key : params.keySet()) {
				logger.debug("params key:  " + key);
				logger.debug("params value:  " + params.get(key));
				if (flag != 0) {
					url += "&";
				}
				String encode = URLEncoder.encode(params.get(key), "utf-8");
				url += (key + "=" + encode);
				logger.debug("params encode value:  " + encode);
				logger.debug("params decode value:  " + URLDecoder.decode(encode, "utf-8"));
				flag++;
			}
		}
		HttpGet httpget = new HttpGet(url);
		for (String key : headerdatas.keySet()) {
			httpget.addHeader(key, headerdatas.get(key));
		}
		httpget.addHeader("Content-Type", "text/html; charset=utf-8");
		logger.info("send  URI:   " + httpget.getURI());
		// logger.info("send URI: "+httpget.getURI().toURL());
		logger.info("send  URI:   " + httpget.getURI().getRawPath());
		// logger.info("send PARAM: "+httpget.getParams());
		// logger.info("send PARAM:
		// "+httpget.getParams().getParameter("param"));
		return httpclient.execute(httpget);
	}

	public static HttpResponse get(String url, String XAuthToken) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		headerdatas.put("X-Auth-Token", XAuthToken);
		return get(url, headerdatas, new HashMap<String, String>());
	}

	/**
	 * 访问get路径，获取返回值
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static JSONObject get(String url, Map<String, String> params) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		HttpResponse response = get(url, headerdatas, params);
		return response != null ? JSONObject.parseObject(getContentInfoFromHTTPResponse(response)) : null;
	}

	public HttpResponse delete(String url, Map<String, String> headerdatas) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDelete httpdelete = new HttpDelete(url);
		for (String key : headerdatas.keySet()) {
			httpdelete.addHeader(key, headerdatas.get(key));
		}
		return httpclient.execute(httpdelete);
	}

	public HttpResponse delete(String url, String XAuthToken) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		headerdatas.put("X-Auth-Token", XAuthToken);
		return this.delete(url, headerdatas);
	}

	public HttpResponse put(String url, Map<String, String> headerdatas, String json) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url);
		for (String key : headerdatas.keySet()) {
			httpput.addHeader(key, headerdatas.get(key));
		}
		if (json != null) {
			HttpEntity entity = new StringEntity(json);
			httpput.setEntity(entity);
		}
		return httpclient.execute(httpput);
	}

	public HttpResponse putFile(String url, Map<String, String> headerdatas, File file) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url);
		for (String key : headerdatas.keySet()) {
			httpput.addHeader(key, headerdatas.get(key));
		}
		if (file != null) {
			HttpEntity entity = new FileEntity(file, "");
			httpput.setEntity(entity);
		}
		return httpclient.execute(httpput);
	}

	public HttpResponse put(String url, String XAuthToken, String json) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		headerdatas.put("X-Auth-Token", XAuthToken);
		return this.put(url, headerdatas, json);
	}

	public static HttpResponse post(String url, Map<String, String> headerdatas, String json) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		for (String key : headerdatas.keySet()) {
			httppost.addHeader(key, headerdatas.get(key));
		}
		if (json != null) {
			StringEntity entity = new StringEntity(json, "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httppost.setEntity(entity);
		}
		httppost.addHeader("Content-Type", "text/html; charset=utf-8");
		return httpclient.execute(httppost);
	}

	public HttpResponse postFile(String url, Map<String, String> headerdatas, File file) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		for (String key : headerdatas.keySet()) {
			httppost.addHeader(key, headerdatas.get(key));
		}
		if (file != null) {
			HttpEntity entity = new FileEntity(file, "");
			httppost.setEntity(entity);
		}
		return httpclient.execute(httppost);
	}

	public static HttpResponse post(String url, String XAuthToken, String json) throws Exception {
		Map<String, String> headerdatas = new HashMap<String, String>();
		headerdatas.put("X-Auth-Token", XAuthToken);
		return post(url, headerdatas, json);
	}

	public Header[] getHeadersFromResponse(HttpResponse response) throws Exception {
		return response.getAllHeaders();
	}

	public String getContentStringFromResponse(HttpResponse response) throws Exception {
		HttpEntity entity = response.getEntity();
		StringBuilder content = new StringBuilder();
		if (entity != null) {
			try {
				InputStream instream = entity.getContent();
				BufferedReader in = new BufferedReader(new InputStreamReader(instream));
				String str = null;
				while ((str = in.readLine()) != null) {
					content.append(str);
				} // end while
				in.close();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return content.toString();
	}

	public static String getValueFromHeadersByKey(String key, Header[] hds) {
		if (hds == null) {
			return null;
		}
		for (Header hd : hds) {
			if (key.equals(hd.getName())) {
				return hd.getValue();
			}
		}
		return null;
	}

	// ---------------------------------------for
	// test-------------------------------------

	/**
	 * just for test.
	 */
	public void printHttpMethodResponse(String method, String url, String XAuthToken) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// HttpGet httpget = new
		// HttpGet("http://172.31.20.84:8774/v1.1/images/detail");
		// httpget.addHeader("X-Auth-Token",
		// "0077b118bd705d4828a2a49e53726708265f43b7");

		// keystone http://172.31.20.87:5000/v2.0/tenants
		// nova http://172.31.20.87:8774/v1.0/servers
		// nova http://172.31.20.87:8774/v1.1/servers
		// glance http://172.31.20.87:9292/v1/images
		// swift http://172.31.20.87:8080/v1/AUTH_1/
		HttpRequestBase mt = null;
		if ("get".equals(method)) {
			mt = new HttpGet(url);
		} else if ("post".equals(method)) {
			mt = new HttpPost(url);
		} else if ("put".equals(method)) {
			mt = new HttpPut(url);
		} else if ("delete".equals(method)) {
			mt = new HttpDelete(url);
		} else if ("head".equals(method)) {
			mt = new HttpHead(url);
		} else {
			throw new Exception("bad http method[" + method + "]!!!");
		}

		mt.addHeader("X-Auth-Token", XAuthToken);

		HttpResponse response = httpclient.execute(mt);

		System.out.println(getAllInfoFromHTTPResponse(response));
	}

	public static String getAllInfoFromHTTPResponse(HttpResponse response) throws Exception {
		Header[] headers = response.getAllHeaders();
		HttpEntity entity = response.getEntity();
		StringBuilder sb = new StringBuilder();
		sb.append("---------------header--------------\n");
		for (Header h : headers) {
			sb.append(h.getName()).append(": ").append(h.getValue()).append("\n");
		}
		sb.append("---------------content--------------\n");
		if (entity != null) {
			try {
				InputStream instream = entity.getContent();
				BufferedReader in = new BufferedReader(new InputStreamReader(instream));
				String str = null;
				while ((str = in.readLine()) != null) {
					sb.append(str);
				} // while
				in.close();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return sb.toString();
	}

	public static String getContentInfoFromHTTPResponse(HttpResponse response) throws Exception {
		HttpEntity entity = response.getEntity();
		StringBuilder sb = new StringBuilder();
		if (entity != null) {
			try {
				InputStream instream = entity.getContent();
				BufferedReader in = new BufferedReader(new InputStreamReader(instream,"UTF-8"));
				String str = null;
				while ((str = in.readLine()) != null) {
					sb.append(str);
				} // while
				in.close();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("http response:  " + sb.toString());
		return sb.toString();
	}

	// 人脸图像倒入
	public static String faceImgInput(String url, String json) {
		// String url =
		// "http://192.168.201.59:9100/face/v1/framework/face_image/repository/picture";
		String headString = "Content-type: application/json ";
		HttpResponse response = null;
		try {
			response = post(url, headString, json);
			System.out.println(json);
			return response != null ? getContentInfoFromHTTPResponse(response) : "";
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}

	// 检索
	public static String queryFaceByNameJson(String url, String json) {
		// String url =
		// "http://192.168.201.59:9200/face/v1/framework/face/query";
		String headString = "Content-type: application/json ";
		HttpResponse response = null;
		try {
			System.out.println(json);
			response = post(url, headString, json);
			return response != null ? getContentInfoFromHTTPResponse(response) : "";
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}

	// 人口库
	public static String renKouKu(String url, String json) {
		// String url =
		// "http://10.11.229.141:8080/wsquery-web/wsquery";
		String headString = "Content-type: application/json ";
		HttpResponse response = null;
		try {
			System.out.println(json);
			response = get(url, "");
			return response != null ? getContentInfoFromHTTPResponse(response) : "";
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}

	public static void main(String[] args) {

	}

	// 查询Http
	public static String query(String url) {
		HttpResponse response = null;
		try {
			response = get(url, "");
			return response != null ? getContentInfoFromHTTPResponse(response) : "";
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}
}
