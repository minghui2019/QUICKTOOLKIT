package com.eplugger.xml.dom4j.test;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
	private String date;
	private String state;
	List<User> users = new ArrayList<User>();
}