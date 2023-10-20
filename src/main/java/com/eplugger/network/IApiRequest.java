package com.eplugger.network;

import java.util.Map;

import top.tobak.utils.HttpRequestUtils;

public interface IApiRequest {
    default String post(Map<String, Object> formMap) throws Exception {
        try {
            return HttpRequestUtils.post(getUrl(), formMap);
        } catch (Exception e) {
            return "";
        }
    }

    default String post(String body, Object... args) throws Exception {
        String url = String.format(getUrl(), args);
        try {
            return HttpRequestUtils.post(url, body);
        } catch (Exception e) {
            return "";
        }
    }

    default String get(Object... args) throws Exception {
        String url = String.format(getUrl(), args);
        try {
            return HttpRequestUtils.get(url);
        } catch (Exception e) {
            return "";
        }
    }

    String getUrl();
}
