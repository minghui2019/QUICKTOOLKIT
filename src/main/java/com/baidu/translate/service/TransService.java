package com.baidu.translate.service;

import java.util.Map;

import com.baidu.translate.api.TransApi;
import com.baidu.translate.utils.MD5;
import com.eplugger.utils.HttpRequestFacade;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransService {
	private TransService() {}

	private static final String APP_ID = "20230406001630427";
	private static final String SECURITY_KEY = "sIxMwt1PpG29tZ9NmL61";
	
	/**
	 * 中文翻译为英文
	 {
		 "from": "en",
		 "to": "zh",
		 "trans_result": [
			 {
				 "src": "apple",
				 "dst": "苹果"
			 }
		 ]
	 }
	 * @param query
	 * @return
	 */
	public static String transTextZh2En(String query) {
		return transText(query, "zh", "en");
	}

	public static String transText(String query, String from, String to) {
		Map<String, Object> params = buildParams(query, APP_ID, SECURITY_KEY, from, to);
		try {
			HttpRequestFacade httpRequestFacade = new HttpRequestFacade(TransApi.TRANS_VIP);
			JsonObject jsonObject = httpRequestFacade.post(params);
			try {
				return jsonObject.get("trans_result").getAsJsonArray().get(0).getAsJsonObject().get("dst").getAsString();
			} catch (NullPointerException e) {
				if ("52001".equals(jsonObject.get("error_code").getAsString())
					|| "52002".equals(jsonObject.get("error_code").getAsString())
					|| "52003".equals(jsonObject.get("error_code").getAsString())
					|| "54000".equals(jsonObject.get("error_code").getAsString())
					|| "54001".equals(jsonObject.get("error_code").getAsString())
					|| "54003".equals(jsonObject.get("error_code").getAsString())
					|| "54004".equals(jsonObject.get("error_code").getAsString())
					|| "54005".equals(jsonObject.get("error_code").getAsString())
					|| "58000".equals(jsonObject.get("error_code").getAsString())
					|| "58001".equals(jsonObject.get("error_code").getAsString())
					|| "58002".equals(jsonObject.get("error_code").getAsString())
					|| "90107".equals(jsonObject.get("error_code").getAsString())) {
					log.error(jsonObject.get("error_msg").getAsString());
					return null;
				}
			}
		} catch (Exception e) {
			log.error("翻译失败！" + e.getMessage());
		}
		return null;
	}

	private static Map<String, Object> buildParams(String query, String appid, String securityKey, String from, String to) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("q", query);
		params.put("from", from);
		params.put("to", to);

		params.put("appid", appid);

		// 随机数
		String salt = String.valueOf(System.currentTimeMillis());
		params.put("salt", salt);

		// 签名
		String src = appid + query + salt + securityKey; // 加密前的原文
		params.put("sign", MD5.md5(src));

		return params;
	}
}
