package com.eplugger.utils;

import java.util.Map;

import com.eplugger.network.IApiRequest;
import com.google.gson.JsonObject;
import top.tobak.utils.GoogleConstant;

public class HttpRequestFacade {
    private IApiRequest api;

    public HttpRequestFacade(IApiRequest api) {
        this.api = api;
    }

    public JsonObject get(Object... args) {
        try {
            return GoogleConstant.GSON_DEFAULT.fromJson(api.get(args), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    public JsonObject post(String body, Object... args) {
        try {
            return GoogleConstant.GSON_DEFAULT.fromJson(api.post(body, args), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    public JsonObject post(Map<String, Object> formMap) {
        try {
            return GoogleConstant.GSON_DEFAULT.fromJson(api.post(formMap), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }
}
