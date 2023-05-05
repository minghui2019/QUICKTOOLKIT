package com.sui;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;
import java.util.List;

import com.sui.pojo.IdName;

public class Client {
    // Base URLs for requests
    public static final String BASE_URL = "https://www.sui.com";
    public static final String LOGIN_URL = "https://login.sui.com";

    // Transaction types
    public static final int TRAN_TYPE_PAYOUT = 1; // Payout
    public static final int TRAN_TYPE_TRANSFER = 2; // Transfer
    public static final int TRAN_TYPE_INCOME = 5; // Income



    private HttpClient httpClient;
    public boolean verbose;
    private AccountBook accountBook;
    private List<IdName> accountBookList;

    // Create a new client with email and password
    public Client(String email, String password) throws Exception {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();
        Login login = new Login();
        login.login(email, password);
    }

    // Create a new client using environment variables for email and password
    // The environment variables for email and password are FEIDEE_USERNAME and FEIDEE_PASSWORD respectively.
    public Client() throws Exception {
        this(System.getenv("FEIDEE_USERNAME"), System.getenv("FEIDEE_PASSWORD"));
    }
}