package com.eplugger.trans.service;

import com.eplugger.util.HttpRequestUtils;

import net.sf.json.JSONObject;

public class TransService {
	/** 获取token地址 */
	private static final String TRANS_HOST = "https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1?access_token=%s";
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
		JSONObject body = new JSONObject();
		body.put("q", query);
		body.put("from", "zh");
		body.put("to", "en");
		body.put("termIds", "");
		JSONObject result = HttpRequestUtils.postHttp(String.format(TRANS_HOST, AuthService.getAuth()), body.toString());
		System.out.println(result.toString());
		return ((JSONObject) result.getJSONObject("result").getJSONArray("trans_result").get(0)).getString("dst");
	}
}
