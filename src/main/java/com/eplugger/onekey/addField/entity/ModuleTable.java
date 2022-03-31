package com.eplugger.onekey.addField.entity;

import java.lang.reflect.Field;

import com.eplugger.annotation.Booleaner;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModuleTable {
	private String moduleName;
	private String tableName;
	@Booleaner
	private Boolean ignore;
	private String described;
	@Override
	public String toString() {
		return "ModuleTable [moduleName=" + moduleName + ", tableName=" + tableName + ", ignore=" + ignore
				+ ", described=" + described + "]";
	}
	
	public static void main(String[] args) {
		ModuleTable moduleTable = new ModuleTable();
		Class<? extends ModuleTable> class1 = moduleTable.getClass();
		Field[] declaredFields = class1.getDeclaredFields();
		for (Field field : declaredFields) {
			System.out.println(field.getName() + ": " + field.isAnnotationPresent(Booleaner.class));
		}
		System.out.println(declaredFields);
	}
}
