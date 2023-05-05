package com.sui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sui.pojo.Category;
import com.sui.pojo.IdName;

@lombok.Data
public class AccountBook {
	private List<Category> categories;
	private List<IdName> stores;
	private List<IdName> members;
	private List<IdName> accounts;
	private List<IdName> projects;

	//根据科目名获取科目ID为索引的Map
	public Map<Integer, Category> categoryIdMap() {
		Map<Integer, Category> m = new HashMap<>();
		for (Category category : categories) {
			m.put(category.getId(), category);
		}
		return m;
	}

	//根据科目名获取科目ID
	public int categoryIdByName(String name) {
		for (Category item : categories) {
			if (item.getName().equals(name)) {
				return item.getId();
			}
		}
		return 0;
	}

	//根据商户名获取商户ID
	public int storeIdByName(String name) {
		for (IdName item : stores) {
			if (item.getName().equals(name)) {
				return item.getId();
			}
		}
		return 0;
	}

	//根据成员名获取成员ID
	public int memberIdByName(String name) {
		for (IdName item : members) {
			if (item.getName().equals(name)) {
				return item.getId();
			}
		}
		return 0;
	}

	//根据账户名获取账户ID
	public int accountIdByName(String name) {
		for (IdName item : accounts) {
			if (item.getName().equals(name)) {
				return item.getId();
			}
		}
		return 0;
	}

	//根据项目名获取项目ID
	public int projectIdByName(String name) {
		for (IdName item : projects) {
			if (item.getName().equals(name)) {
				return item.getId();
			}
		}
		return 0;
	}
}