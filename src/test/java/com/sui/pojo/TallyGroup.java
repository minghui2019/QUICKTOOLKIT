package com.sui.pojo;

import java.util.List;

import com.sui.Tally;

// Transaction group (usually grouped by day)
public class TallyGroup {
    public IncomeAndPayout incomeAndPayout;
    public List<Tally> list;
}