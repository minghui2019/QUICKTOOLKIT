package com.sui.pojo;

import java.util.List;

// Response for transaction query interface
public class TallyResponseInfo {
    public IncomeAndPayout incomeAndPayout;
    public int pageNo;
    public int pageCount;
    public String symbol;
    public String endDate;
    public String beginDate;
    public List<TallyGroup> groups;
}
