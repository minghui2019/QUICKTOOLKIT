package com.sui.pojo;

import java.util.List;

import lombok.Data;

@Data
public class Category extends IdName {
	private int type; // Category type: expenditure or income
	private boolean isSub; // Whether it is a subcategory
	private List<Integer> subIds;


}
