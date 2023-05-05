package com.sui;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class Login {
    // Maximum recursive call count when automatically redirecting to refresh authentication information
    public static final int MAX_AUTH_REDIRECT_COUNT = 5;

    // Login
    public void login(String email, String password) throws Exception {
        VCCodeInfo vccodeInfo = getVccode();
        verifyUser(vccodeInfo, email, password);
        authRedirect("GET", LOGIN_URL + "/auth.do", null, 0);
        syncAccountBookList();
    }

    public static class VCCodeInfo {
        public String VCCode;
        public String uid;
    }

    // Get VCCode
    public VCCodeInfo getVccode() throws Exception {
        HttpResponse<String> resp = get(LOGIN_URL + "/login.do?opt=vccode");
        if (resp.statusCode() != 200) {
            throw new Exception("Error requesting VCCode: " + resp.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        VCCodeInfo respInfo = mapper.readValue(resp.body(), VCCodeInfo.class);

        if (respInfo.VCCode == null || respInfo.VCCode.isEmpty()) {
            throw new Exception("No suitable VCCode found");
        }

        return respInfo;
    }

    // Verify user
    public void verifyUser(VCCodeInfo vccodeInfo, String email, String password) throws Exception {
        // Encrypt password
        password = hexSha1(password);
        password = hexSha1(email + password);
        password = hexSha1(password + vccodeInfo.VCCode);

        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("status", "1"); // Whether to keep login status: 0 do not keep, 1 keep
        data.put("password", password);
        data.put("uid", vccodeInfo.uid);

        HttpResponse<String> resp = get(LOGIN_URL + "/login.do?" + mapToQueryString(data));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> respInfo = mapper.readValue(resp.body(), Map.class);

        String status = respInfo.get("Status");
        switch (status) {
            case "ok":
                return;
            case "no":
                throw new Exception("Incorrect username or password");
            case "lock":
                throw new Exception("Too many incorrect password attempts, try again in ten minutes");
            case "lock-status":
                throw new Exception("This account has been locked due to abnormal behavior, please appeal on the official website");
            default:
                throw new Exception("Unknown status: " + status);
        }
    }

    private static String hexSha1(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(s.getBytes());
        return bytesToHex(digest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Automatically follow authentication redirects to complete verification information refresh
    public void authRedirect(String method, String address, Map<String, String> data, int jumpCount) throws Exception {
        if (verbose) {
            System.out.println("Authentication redirect " + jumpCount + " times " + method + " " + address + " parameters " + data);
        }
        if (jumpCount > MAX_AUTH_REDIRECT_COUNT) {
            throw new Exception("Too many redirects");
        }

        HttpResponse<String> resp;
        if (method.equals("POST")) {
            resp = postForm(address, data);
        } else if (method.equals("GET")) {
            resp = get(address + "?" + mapToQueryString(data));
        } else {
            throw new Exception("Unknown redirect method " + method);
        }

        Document doc = Jsoup.parse(resp.body());

        // Parse body tag, if no further redirect is needed, return
        String onload = doc.selectFirst("body").attr("onload");
        if (!onload.equals("document.forms[0].submit()")) {
            return;
        }

        // Otherwise parse the redirect form and recursively redirect
        Element form = doc.selectFirst("form");

        String formMethod = form.attr("method");
        String formAction = form.attr("action");

        Map<String, String> formData = new HashMap<>();
        Elements inputs = form.select("input");
        for (Element input : inputs) {
            String name = input.attr("name");
            String value = input.attr("value");

            if (!name.isEmpty()) {
                formData.put(name, value);
            }
        }

        authRedirect(formMethod, formAction, formData, jumpCount + 1);
    }
}