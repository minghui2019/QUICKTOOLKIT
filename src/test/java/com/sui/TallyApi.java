package com.sui;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sui.pojo.IncomeAndPayout;
import com.sui.pojo.TallyGroup;
import com.sui.pojo.TallyResponseInfo;

public class TallyApi {
    //获取流水，可用参数包括
	//    bids账户、cids科目、mids类型、pids项目、sids商户、memids成员，这几个参数都是逗号分割的ID列表
	//    order:  排序字段，支持: project_id项目排序、buyer_name账户、item_amount金额、tran_type类型、category_id科目、tran_time时间
	//    isDesc: 是否降序，0升序、1降序
	//    note:   搜备注关键字
	public TallyResponseInfo tallyList(LocalDate begin, LocalDate end, Map<String, String> data) throws Exception {
        // First get information from all pages to form a list
        int pageCount = 1;
        List<TallyResponseInfo> infos = new ArrayList<>();
        for (int page = 1; page <= pageCount; page++) {
            TallyResponseInfo pageInfo = tallyListByPage(begin, end, data, page);
            pageCount = pageInfo.pageCount;
            infos.add(pageInfo);
        }

        // If there are no transaction records, return directly
        if (infos.isEmpty()) {
            return new TallyResponseInfo();
        }

        // Since the system pagination is based on quantity, it is possible for the same day to be scattered into two groups
        // So discard the original group and re-fill the tally information in the group into the map according to the date
        // The key of the map is an integer composed of year, month and day, such as 20170707, which is more efficient than a string
        Map<Integer, List<Tally>> tallyMap = new HashMap<>();
        for (TallyResponseInfo info : infos) {
            for (TallyGroup group : info.groups) {
                for (Tally tally : group.list) {
                    int key = (tally.date.year + 1900) * 10000;
                    key += (tally.date.month + 1) * 100;
                    key += tally.date.date;

                    List<Tally> tallies = tallyMap.getOrDefault(key, new ArrayList<>());
                    tallies.add(tally);
                    tallyMap.put(key, tallies);
                }
            }
        }

        // Traverse the map of tallies to reorganize groups and response info
        int dateMax = 0;
        int dateMin = 99999999;
        TallyResponseInfo mergedInfo = new TallyResponseInfo();
        mergedInfo.groups = new ArrayList<>();
        for (Map.Entry<Integer, List<Tally>> entry : tallyMap.entrySet()) {
            int t = entry.getKey();
            List<Tally> tallies = entry.getValue();

            if (t > dateMax) {
                dateMax = t;
            }
            if (t < dateMin) {
                dateMin = t;
            }
            TallyGroup group = new TallyGroup();
            group.list = new ArrayList<>();
            for (Tally tally : tallies) {
                if (tally.tranType == Client.TRAN_TYPE_PAYOUT) {
                    group.incomeAndPayout.payout += tally.itemAmount;
                } else if (tally.tranType == Client.TRAN_TYPE_INCOME) {
                    group.incomeAndPayout.income += tally.itemAmount;
                }
                group.list.add(tally);
            }
            mergedInfo.incomeAndPayout.income += group.incomeAndPayout.income;
            mergedInfo.incomeAndPayout.payout += group.incomeAndPayout.payout;

            // Since the group is reorganized, the order of records will change
            // Here it is temporarily sorted by time. Later it will be changed to determine the sorting method according to the incoming parameters.
            group.list.sort(Comparator.comparingInt(t1 -> t1.date.time));
            mergedInfo.groups.add(group);
        }

        mergedInfo.beginDate = String.format("%4d.%02d.%02d", dateMin / 10000, (dateMin / 100) % 100, dateMin % 100);
        mergedInfo.endDate = String.format("%4d.%02d.%02d", dateMax / 10000, (dateMax / 100) % 100, dateMax % 100);

        return mergedInfo;
    }

    public TallyResponseInfo tallyListByPage(LocalDate begin, LocalDate end, Map<String, String> data, int page) throws Exception {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put("opt", "list2");
        data.put("page", Integer.toString(page));

        if (begin != null) {
            data.put("beginDate", begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        if (end != null) {
            data.put("endDate", end.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        // Some parameters must have default values
        if (!data.containsKey("bids")) {
            data.put("bids", "0");
        }
        if (!data.containsKey("cids")) {
            data.put("cids", "0");
        }
        if (!data.containsKey("mids")) {
            data.put("mids", "0");
        }
        if (!data.containsKey("pids")) {
            data.put("pids", "0");
        }
        if (!data.containsKey("sids")) {
            data.put("sids", "0");
        }
        if (!data.containsKey("memids")) {
            data.put("memids", "0");
        }

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/tally/new.rmi", data);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resp.body(), TallyResponseInfo.class);
    }

    // Get monthly summary of income and payout, key is in the format of 201707
    public Map<Integer, IncomeAndPayout> monthIncomeAndPayoutMap(int beginYear, int endYear) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("opt", "someYearSum");
        data.put("endYear", Integer.toString(endYear));
        data.put("beginYear", Integer.toString(beginYear));

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/tally/new.rmi", data);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, IncomeAndPayout>> respInfo = mapper.readValue(resp.body(), new TypeReference<Map<String, Map<String, IncomeAndPayout>>>() {});

        Map<Integer, IncomeAndPayout> infoMap = new HashMap<>();
        for (Map.Entry<String, Map<String, IncomeAndPayout>> entry : respInfo.entrySet()) {
            String yearKey = entry.getKey();
            Map<String, IncomeAndPayout> yearInfo = entry.getValue();

            for (Map.Entry<String, IncomeAndPayout> subEntry : yearInfo.entrySet()) {
                String monthKey = subEntry.getKey();
                IncomeAndPayout monthInfo = subEntry.getValue();

                int year = Integer.parseInt(yearKey);
                int month = Integer.parseInt(monthKey);

                int key = year * 100 + month;
                infoMap.put(key, monthInfo);
            }
        }

        return infoMap;
    }

    // Update transaction interface
    public void tallyUpdate(Tally tally, Map<String, String> updateData) throws Exception {
        Map<String, String> data = tally.toUpdateParams();
        for (Map.Entry<String, String> entry : updateData.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }

        String tranType;
        switch (tally.tranType) {
            case Client.TRAN_TYPE_PAYOUT:
                tranType = "payout";
                break;
            case Client.TRAN_TYPE_TRANSFER:
                tranType = "transfer";
                break;
            case Client.TRAN_TYPE_INCOME:
                tranType = "income";
                break;
            default:
                throw new Exception("Unknown transaction type " + tally.tranType);
        }

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/tally/" + tranType + ".rmi", data);

        if (resp.body().equals("{result:'ok'}")) {
            return;
        }

        throw new Exception("Error: " + resp.body());
    }

    // Add transaction interface
    public void tallyCreate(Tally tally, LocalDateTime when) throws Exception {
        Map<String, String> data = new HashMap<>();

        // Always 0 when adding
        data.put("id", "0");

        data.put("memo", tally.memo);

        data.put("store", Integer.toString(tally.storeId));
        data.put("category", Integer.toString(tally.categoryId));
        data.put("project", Integer.toString(tally.projectId));
        data.put("member", Integer.toString(tally.memberId));

        data.put("time", when.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        data.put("price", Float.toString(tally.itemAmount));

        // TODO: The following fields are unclear
        //data.put("url", "");
        //data.put("price2", "");
        //data.put("in_account", "");
        //data.put("out_account", "");
        //data.put("debt_account", "");

        String targetUri;
        Pattern apiResponsePattern = Pattern.compile("id:{id:[0-9]+},");

        if (tally.tranType == Client.TRAN_TYPE_INCOME) {
            targetUri = Client.BASE_URL + "/tally/income.rmi";
            data.put("account", Integer.toString(tally.account));
        } else if (tally.tranType == Client.TRAN_TYPE_PAYOUT) {
            targetUri = Client.BASE_URL + "/tally/payout.rmi";
            data.put("account", Integer.toString(tally.account));
        } else if (tally.tranType == Client.TRAN_TYPE_TRANSFER) {
            targetUri = Client.BASE_URL + "/tally/transfer.rmi";
            apiResponsePattern = Pattern.compile("id:{outId:\\d+,inId:\\d+}");
            data.put("in_account", Integer.toString(tally.sellerAcountId));
            data.put("out_account", Integer.toString(tally.buyerAcountId));
        } else {
            throw new Exception("Unsupported transaction type: " + tally.tranType);
        }

        HttpResponse<String> resp = HttpClient.postForm(targetUri, data);

        // Check if the returned data is valid
        if (apiResponsePattern.matcher(resp.body()).find()) {
            return;
        }

        throw new Exception(resp.body());
    }

// (Batch) delete transaction interface
    public void tallyDelete(String... tranIds) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("opt", "batchDel");

        StringJoiner joiner = new StringJoiner(",");
        for (String tranId : tranIds) {
            joiner.add(tranId);
        }
        data.put("ids", joiner.toString());

        HttpResponse<String> resp = HttpClient.postForm(Client.BASE_URL + "/tally/new.rmi", data);

        // Returns irregular data containing the number of deleted records
        if (resp.body().equals("{result:'" + tranIds.length + "'}")) {
            return;
        }

        throw new Exception(resp.body());
    }
}