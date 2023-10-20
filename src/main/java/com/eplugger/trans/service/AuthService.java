package com.eplugger.trans.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import top.tobak.common.lang.StringUtils;
import com.eplugger.trans.api.BaidubceApi;
import com.eplugger.utils.HttpRequestFacade;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;


/**
 * 获取token类
 * 官网获取的 API Key: EN46EdO9KCPS9CtppYIaGzaP
 * 官网获取的 Secret Key: onkAHr1QFlf22PCuueoez9ncOOb7lIHO
 */
@Slf4j
public class AuthService {
	private static final String PROPERTIES_FILE = "src/main/resource/baidubceAuth.properties";
	private AuthService() {}
	
	public static void main(String[] args) throws IOException {
		log.debug(getAuth());
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
			String clientId = pro.getProperty("clientId");
			String clientSecret = pro.getProperty("clientSecret");
			endDate = new Date();
			if (StringUtils.isNotBlank(time)) {
				beginDate = DateUtil.parseDateTime(time);
				long between = DateUtil.between(beginDate, endDate, DateUnit.SECOND);
				in.close();
				if (between < 2592000l) {
					return pro.getProperty("token");
				}
			}
			String auth = getAuth(clientId, clientSecret);
			pro.put("clientId", clientId);
			pro.put("clientSecret", clientSecret);
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
			HttpRequestFacade httpRequestFacade = new HttpRequestFacade(BaidubceApi.OAUTH_TOKEN);
			JsonObject jsonObject = httpRequestFacade.get(ak, sk);
			return jsonObject.get("access_token").getAsString();
		} catch (Exception e) {
			log.error("获取token失败！" + e.getMessage());
		}
        return null;
    }
}