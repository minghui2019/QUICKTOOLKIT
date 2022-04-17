package com.eplugger.util;

import java.util.HashMap;
import java.util.Map;

import com.eplugger.commons.lang3.StringUtils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import net.sf.json.JSONObject;

public class HttpRequestUtils {
	/**
	 * POST请求
	 * @param url
	 * @param body
	 * @return
	 */
	public static JSONObject postHttp(String url, String body) {
		String result = HttpRequestUtils.post(url, body);
		if (StringUtils.isNotBlank(result)) {
			return JSONObject.fromObject(result);
		} else {
			return new JSONObject();
		}
	}
	
	/**
	 * <pre>可以设置头的post请求，
	 * <pre>默认头"Content-Type", "application/json;charset=utf-8"
	 * <pre>默认超时时长，-1表示默认超时时长
	 * @param url
	 * @param body
	 * @return
	 */
	public static String post(String url, String body) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json;charset=utf-8");
		return HttpRequestUtils.post(url, headers, body);
	}
	

	/**
	 * <pre>可以设置头的post请求
	 * <pre>默认超时时长，-1表示默认超时时长
	 * @param url
	 * @param headers
	 * @param body
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String body) {
		return HttpRequestUtils.post(url, headers, body, HttpRequest.TIMEOUT_DEFAULT);
	}
	
	/**
	 * <pre>可以设置头的post请求
	 * @param url
	 * @param headers
	 * @param body
	 * @param timeout
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String body, int timeout) {
		return new HttpRequest(url).method(Method.POST).addHeaders(headers).timeout(timeout).body(body).execute().body();
	}
}
