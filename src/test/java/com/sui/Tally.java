package com.sui;

import java.util.HashMap;
import java.util.Map;

import com.sui.pojo.DateInfo;


public class Tally {
    // Transaction information
	public int account;
	public String buyerAcount; // Payout/income account, transfer account for transfers
	public int buyerAcountId; // Payout/income account, transfer account for transfers
	public int categoryId;
	public String categoryName;
	public int memberId;
	public String memberName;
	public int tranType;
	public String tranName;
	public int projectId;
	public String projectName;
	public int storeId; // Merchant ID
	public String sellerAcount; // Transfer account for transfers
	public int sellerAcountId; // Transfer account for transfers
	
	public float itemAmount; // Transaction amount, same as currencyAmount for transfers
	public float currencyAmount; // Transfer amount
	
	public String relation;
	public String categoryIcon;
	public String url;
	public String content;
	public int imgId;
	public int tranId;
	public String memo;
	
	public DateInfo date;
	
	// Generate url.Values parameters for updating
	public Map<String, String> toUpdateParams() {
		Map<String, String> data = new HashMap<>();
		
		data.put("id", Integer.toString(tranId));
		
		if (tranType == com.sui.Client.TRAN_TYPE_TRANSFER) {
			data.put("in_account", Integer.toString(sellerAcountId));
			data.put("out_account", Integer.toString(buyerAcountId));
		} else {
			data.put("account", Integer.toString(account));
		}
		
		data.put("store", Integer.toString(storeId));
		
		data.put("category", Integer.toString(categoryId));
		data.put("project", Integer.toString(projectId));
		data.put("member", Integer.toString(memberId));
		
		data.put("memo", memo);
		data.put("url", url);
		
		String strTime = String.format("%d.%d.%d %d:%d:%d",
				1900 + date.year,
				date.month + 1,
				date.date,
				date.hours,
				date.minutes,
				date.seconds);
		data.put("time", strTime);
		
		data.put("price", Float.toString(itemAmount));
		data.put("price2", Float.toString(currencyAmount));
		
		return data;
	}
}