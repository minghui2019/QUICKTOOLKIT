package com.eplugger.enums;

import com.eplugger.utils.DBUtils;

public enum DataType {
	STRING("String"),
	INTEGER("Integer"),
	DOUBLE("Double"),
	DATE("Date"),
	TIMESTAMP("Timestamp"),
	LIST("List"),
	ARRAYLIST("ArrayList");
	
	public String java;
	String dataBase;
	
	DataType(String java) {
		this.java = java;
		switch (java) {
		case "String":
			this.dataBase = DBUtils.isSqlServer() ? "varchar" : "VARCHAR2";
			break;
		case "Timestamp":
		case "Date":
			this.dataBase = DBUtils.isSqlServer() ? "datetime" : "DATE";
			break;
		case "Double":
			this.dataBase = DBUtils.isSqlServer() ? "decimal(18,6)" : "NUMBER(18,6)";
			break;
		case "Integer":
			this.dataBase = DBUtils.isSqlServer() ? "int" : "NUMBER(18,6)";
			break;
		default:
			break;
		}
	}
}
