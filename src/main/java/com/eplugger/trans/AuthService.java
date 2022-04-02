package com.eplugger.trans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.eplugger.util.HttpRequestUtils;
import com.eplugger.util.StringUtils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import net.sf.json.JSONObject;


/**
 * 获取token类
 */
public class AuthService {
	/** 官网获取的 API Key */
	private static final String CLIENT_ID = "EN46EdO9KCPS9CtppYIaGzaP";
	/** 官网获取的 Secret Key */
	private static final String CLIENT_SECRET = "onkAHr1QFlf22PCuueoez9ncOOb7lIHO";
	/** 获取token地址 */
	private static final String AUTH_HOST = "https://aip.baidubce.com/oauth/2.0/token";
	private static final String PROPERTIES_FILE = "src/main/resource/baiduAuth.properties";
	
	public static void main(String[] args) throws IOException {
		getAuth();
	}

    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
    	Date beginDate, endDate;
    	Properties pro = new Properties();
    	
		try {
			FileInputStream in = new FileInputStream(PROPERTIES_FILE);
			pro.load(in);
			String time = pro.getProperty("time");
			endDate = new Date();
			if (StringUtils.isNotBlank(time)) {
				beginDate = DateUtil.parseDateTime(time);
				long between = DateUtil.between(beginDate, endDate, DateUnit.SECOND);
				in.close();
				if (between < 2592000l) {
					return pro.getProperty("token");
				}
			}
			String auth = getAuth(CLIENT_ID, CLIENT_SECRET);
			pro.put("token", auth);
			pro.put("time", DateUtil.formatDateTime(endDate));
			FileOutputStream out = new FileOutputStream(PROPERTIES_FILE);
			pro.store(out, null);
			out.close();
			return auth;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Secret Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
    	try {
    		JSONObject jsonObject = HttpRequestUtils.postHttp(AUTH_HOST, "grant_type=client_credentials" + "&client_id=" + ak + "&client_secret=" + sk);
    		return jsonObject.getString("access_token");
		} catch (Exception e) {
			System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
		}
        return null;
    }
}