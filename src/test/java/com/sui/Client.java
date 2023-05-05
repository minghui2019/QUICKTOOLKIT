package com.sui;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;
import java.util.List;

public class Client {
    // Base URLs for requests
    public static final String BASE_URL = "https://www.sui.com";
    public static final String LOGIN_URL = "https://login.sui.com";

    // Transaction types
    public static final int TRAN_TYPE_PAYOUT = 1; // Payout
    public static final int TRAN_TYPE_TRANSFER = 2; // Transfer
    public static final int TRAN_TYPE_INCOME = 5; // Income

    // Page structure used in multiple responses
    public static class PageInfo {
        public int pageCount;
        public int pageNo;
    }

    // Date structure used in multiple responses
    public static class DateInfo {
        public int year; // Nth year since 1900
        public int month; // Month starting from 0
        public int date; // Date
        public int day; // Day of the week
        public int hours; // Hours in Beijing time
        public int minutes; // Minutes
        public int seconds; // Seconds
        public int time; // Unix timestamp * 1000
        public int timezoneOffset; // Difference in hours from UTC time
    }

    // Structure containing ID and Name properties
    public static class IdName {
        public int id;
        public String name;

        public IdName(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // Structure containing Income and Payout properties
    public static class IncomeAndPayout {
        public float income;
        public float payout;
    }

    private HttpClient httpClient;
    private boolean verbose;
    private AccountBook accountBook;
    private List<IdName> accountBookList;

    // Create a new client with email and password
    public Client(String email, String password) throws Exception {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();
        login(email, password);
    }

    // Create a new client using environment variables for email and password
    // The environment variables for email and password are FEIDEE_USERNAME and FEIDEE_PASSWORD respectively.
    public Client() throws Exception {
        this(System.getenv("FEIDEE_USERNAME"), System.getenv("FEIDEE_PASSWORD"));
    }
}