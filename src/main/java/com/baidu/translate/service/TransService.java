package com.baidu.translate.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baidu.translate.api.TransApi;
import com.baidu.translate.utils.MD5;
import com.eplugger.utils.HttpRequestFacade;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.tobak.utils.GoogleConstant;

@Slf4j
public class TransService {
	private TransService() {}

	private static final String APP_ID = "20230406001630427";
	private static final String SECURITY_KEY = "sIxMwt1PpG29tZ9NmL61";

	public static Map<String, String> transTextZh2En(List<String> query) {
		Map<String, String> result = Maps.newHashMap();
		List<String> transTexts = transText(query, "zh", "en");
		for (int i= 0; i<query.size(); i++) {
			result.put(query.get(i), transTexts.get(i));
		}
		return result;
	}

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
	public static List<String> transTextZh2En(String query) {
		return transText(query, "zh", "en");
	}

	public static List<String> transTextZh2En(String[] query) {
		return transText(Lists.newArrayList(query), "zh", "en");
	}

	public static List<String> transText(String query, String from, String to) {
		query = query.replace("，", ",").replace("、", ",").replace("；", ",").replace(";", ",");
		return transText(GoogleConstant.SPLITTER_COMMA.splitToList(query), from, to);
	}

	public static List<String> transText(List<String> query, String from, String to) {
		Map<String, Object> params = buildParams(query.stream().collect(Collectors.joining("\n\r")), APP_ID, SECURITY_KEY, from, to);
		try {
			HttpRequestFacade httpRequestFacade = new HttpRequestFacade(TransApi.TRANS_VIP);
			JsonObject jsonObject = httpRequestFacade.post(params);
			log.info(jsonObject.toString());
			try {
				JsonElement element = jsonObject.get("trans_result");
				JsonArray transResult = element.getAsJsonArray();
				List<String> transList = Lists.newArrayList();
				for (JsonElement jsonElement : transResult) {
					String dst = jsonElement.getAsJsonObject().get("dst").getAsString();
					transList.add(dst);
				}
				return transList;
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
