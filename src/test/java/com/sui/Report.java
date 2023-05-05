package com.sui;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {
    // Reconciliation report
    public static class CompareInfo {
        public float balance; // Current balance
        public float dayBalance; // Daily balance (daily income - daily payout)
        public Money money;
        public DateInfo date;

        public static class Money {
            public float claims;
            public float income;
            public float in; // Inflow
            public float debet;
            public float out; // Outflow
            public float payout;
        }
    }

    // Report page response
    public static class CompareReportResponse {
        public PageInfo pageInfo;
        public List<CompareInfo> list;
    }

    // Get all reconciliation reports
    public List<CompareInfo> compareReport(int accountId, LocalDate begin, LocalDate end) throws Exception {
        int pageCount = 1;
        List<CompareInfo> list = new ArrayList<>();
        for (int page = 1; page <= pageCount; page++) {
            CompareReportResponse info = compareReportByPage(accountId, begin, end, page);
            pageCount = info.pageInfo.pageCount;
            list.addAll(info.list);
        }

        return list;
    }

    // Get single page reconciliation report
    public CompareReportResponse compareReportByPage(int accountId, LocalDate begin, LocalDate end, int page) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("m", "compare");
        data.put("page", Integer.toString(page));
        data.put("cardId", Integer.toString(accountId));
        data.put("endDate", end.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        data.put("beginDate", begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

        HttpResponse<String> resp = postForm(BASE_URL + "/report.rmi", data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resp.body(), CompareReportResponse.class);
    }

    // Daily income and payout report
    public static class DailyReportList extends ArrayList<DailyReportList.Item> {
        public static class Item {
            public IdName idName;
            public float total;
            public List<SubItem> list;

            public static class SubItem {
                public IdName idName;
                public float amount;
            }
        }
    }

    public static class DailyReport {
        public float inAmount;
        public float outAmount;

        public String symbol;
        public float maxIn;
        public float maxOut;

        public DailyReportList inList;
        public DailyReportList outList;
    }

    // Get daily income and payout report
    public DailyReport dailyReport(LocalDate begin, LocalDate end, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("m", "daily");
        params.put("endDate", end.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        params.put("beginDate", begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

        HttpResponse<String> resp = postForm(BASE_URL + "/report.rmi", params);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resp.body(), DailyReport.class);
    }
}