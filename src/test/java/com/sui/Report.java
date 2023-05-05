package com.sui;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sui.pojo.CompareInfo;
import com.sui.pojo.CompareReportResponse;
import com.sui.pojo.DailyReport;


public class Report {
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

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/report.rmi", data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resp.body(), CompareReportResponse.class);
    }

    
    // Get daily income and payout report
    public DailyReport dailyReport(LocalDate begin, LocalDate end, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("m", "daily");
        params.put("endDate", end.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        params.put("beginDate", begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/report.rmi", params);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resp.body(), DailyReport.class);
    }
}