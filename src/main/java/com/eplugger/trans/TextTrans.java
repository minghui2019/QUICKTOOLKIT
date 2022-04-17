package com.eplugger.trans;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.util.HttpRequestUtils;

import net.sf.json.JSONObject;

public class TextTrans {
	/** 获取token地址 */
	private static final String TRANS_HOST = "https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1?access_token=%s";
	
	public static void main(String[] args) {
		String result = transText2En("主项目、项目类型、项目介绍、分管副院长");
		//Patent source, transferor, country, contact person of agency, contact number of agency
//		String result = "Patent source, transferor, country, contact person of agency, contact number of agency";
		String[] split = result.split(",");
		String[] transText2En = transText2En(split);
		System.out.println(Arrays.toString(transText2En));
	}
	
	private static Pattern patternOf = Pattern.compile("([\\w ]+) [o|O]f ([\\w ]+)");
	public static String[] transText2En(String[] resurces) {
		String[] dests = new String[resurces.length];
		for (int i = 0; i < resurces.length; i++) {
			String resurce = resurces[i].trim();
			if (resurce.indexOf("of") != -1) {
				Matcher matcher = patternOf.matcher(resurce);
				if (matcher.matches()) {
					resurce = matcher.group(2) + " " + matcher.group(1);
					System.out.println(matcher.group(2) + " " + matcher.group(1));
				}
			}
			
			String[] resurceArr = resurce.split(" ");
			String dest = StringUtils.firstCharLowerCase(resurceArr[0]);
			for (int j = 1; j < resurceArr.length; j++) {
				dest += StringUtils.firstCharUpperCase(resurceArr[j]);
			}
			dests[i] = dest;
		}
		return dests;
	}
	
	/*
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
