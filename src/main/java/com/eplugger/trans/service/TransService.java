package com.eplugger.trans.service;

import com.eplugger.trans.api.BaidubceApi;
import com.eplugger.utils.HttpRequestFacade;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransService {
	private TransService() {}
	
	/**
	 * 中文翻译为英文
	 {
	    "result":
	    {
	        "from": "zh",
	        "trans_result":
	        [
	            {
	                "dst": "Patent source, transferor, country, contact person of agency, contact number of agency",
	                "src": "专利来源、转让单位、国别、代理机构联系人、代理机构联系人电话"
	            }
	        ],
	        "to": "en"
	    },
	    "log_id": 1511883485018842480
	}
	 * @param query
	 * @return
	 */
	public static String transText2En(String query) {
		JsonObject body = new JsonObject();
		body.addProperty("q", query);
		body.addProperty("from", "zh");
		body.addProperty("to", "en");
		body.addProperty("termIds", "");
		try {
			HttpRequestFacade httpRequestFacade = new HttpRequestFacade(BaidubceApi.TEXT_TRANS);
			JsonObject jsonObject = httpRequestFacade.post(body.toString(), AuthService.getAuth());
			/**
			 * {"error_code":18,"error_msg":"Open api qps request limit reached"}
			 */
			if (jsonObject.get("error_code").getAsInt() == 18) {
				log.error(jsonObject.get("error_msg").getAsString());
				return null;
			}
			return jsonObject.get("result").getAsJsonObject()
				.get("trans_result").getAsJsonArray().get(0).getAsJsonObject()
				.get("dst").getAsString();
		} catch (Exception e) {
			log.error("获取token失败！" + e.getMessage());
		}
		return null;
	}
}
