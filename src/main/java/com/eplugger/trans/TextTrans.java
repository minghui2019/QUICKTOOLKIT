package com.eplugger.trans;

import com.eplugger.util.HttpRequestUtils;

import net.sf.json.JSONObject;

public class TextTrans {
	/** 获取token地址 */
	private static final String TRANS_HOST = "https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1?access_token=%s";
	
	public static void main(String[] args) {
		String transText2En = transText2En("你好,管理,爱好");
		System.out.println(transText2En);
	}
	
	public static String transText2En(String query) {
		JSONObject body = new JSONObject();
		body.put("q", query);
		body.put("from", "zh");
		body.put("to", "en");
		body.put("termIds", "");
		JSONObject result = HttpRequestUtils.postHttp(String.format(TRANS_HOST, AuthService.getAuth()), body.toString());
		return ((JSONObject) result.getJSONObject("result").getJSONArray("trans_result").get(0)).getString("dst");
		
	}
}
