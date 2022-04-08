package com.eplugger.xml.dom4j.test;

import com.eplugger.annotation.dom4j.Dom4jAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Dom4jAsAttribute
	private int id;
	private String name;
	private int age;
	private String sex;
}