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
	
	private DataType(String java) {
		this.java = java;
		switch (this.java) {
		case "String":
			this.dataBase = DBUtils.isSqlServer() ? "varchar" : "VARCHAR2";
//			if (precision == null) {
//				result += "(255)";
//			} else if (precision >= 2000) {
//				result = DBUtils.isSqlServer() ? "text" : "clob";
//			} else {
//				result += "(" + precision + ")";
//			}
			break;
		case "Timestamp":
			this.dataBase = DBUtils.isSqlServer() ? "datetime" : "DATE";
			break;
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
