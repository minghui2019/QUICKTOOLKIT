package com.eplugger.utils;

import java.util.Map;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.eplugger.common.lang.StringUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public class HttpRequestUtils {
	private HttpRequestUtils() {}

	/******************************** POST ********************************/

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
	 * <pre>
	 *     可以设置头的post请求
	 *     默认头"Content-Type", "application/json;charset=utf-8"
	 *     默认超时时长，-1表示默认超时时长
	 * </pre>
	 * @param url
	 * @param body
	 * @return
	 */
	public static String post(String url, String body) {
		Map<String, String> headers = Maps.newHashMap();
		headers.put("Content-Type", "application/json;charset=utf-8");
		return post(url, headers, body);
	}

	/**
	 * <pre>
	 *     可以设置头的post请求
	 *     默认超时时长，{@link HttpRequest#TIMEOUT_DEFAULT}
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param body 请求体
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String body) {
		return new HttpRequest(url).method(Method.POST).addHeaders(headers).timeout(HttpRequest.TIMEOUT_DEFAULT).body(body).execute().body();
	}

	/**
	 * <pre>
	 *     可以设置头的post请求
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param body 请求体
	 * @param timeout 设置超时，单位：毫秒
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String body, int timeout) {
		return new HttpRequest(url).method(Method.POST).addHeaders(headers).timeout(timeout).body(body).execute().body();
	}

	/**
	 * <pre>
	 *     可以设置头的post请求
	 *     默认超时时长，{@link HttpRequest#TIMEOUT_DEFAULT}
	 * </pre>
	 * @param url 请求地址
	 * @param paramMap 请求参数
	 * @return
	 */
	public static String post(String url, Map<String, Object> paramMap) {
		return post(url, null, paramMap);
	}

	/**
	 * <pre>
	 *     可以设置头的post请求
	 *     默认超时时长，{@link HttpRequest#TIMEOUT_DEFAULT}
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param paramMap 请求参数
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> paramMap) {
		return new HttpRequest(url).method(Method.POST).addHeaders(headers).form(paramMap).timeout(HttpRequest.TIMEOUT_DEFAULT).execute().body();
	}

	/**
	 * <pre>
	 *     可以设置头的post请求
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param paramMap 请求参数
	 * @param timeout 设置超时，单位：毫秒
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> paramMap, int timeout) {
		return new HttpRequest(url).method(Method.POST).addHeaders(headers).form(paramMap).timeout(timeout).execute().body();
	}

	/******************************** GET ********************************/

	public static String get(String url) {
		return get(url, null);
	}

	/**
	 * <pre>
	 *     可以设置头的get请求
	 *     默认超时时长，{@link HttpRequest#TIMEOUT_DEFAULT}
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @return
	 */
	public static String get(String url, Map<String, String> headers) {
		return get(url, headers, null);
	}

	/**
	 * <pre>
	 *     可以设置头的get请求
	 *     默认超时时长，{@link HttpRequest#TIMEOUT_DEFAULT}
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param formMap
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, Map<String, Object> formMap) {
		return get(url, headers, formMap, HttpRequest.TIMEOUT_DEFAULT);
	}

	/**
	 * <pre>
	 *     可以设置头的get请求
	 * </pre>
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param formMap
	 * @param timeout 设置超时，单位：毫秒
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, Map<String, Object> formMap, int timeout) {
		String body = HttpRequest.get(url).addHeaders(headers).form(formMap).timeout(timeout).execute().body();
		log.debug(body);
		return body;
	}
}
