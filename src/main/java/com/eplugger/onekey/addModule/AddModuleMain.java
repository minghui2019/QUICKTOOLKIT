package com.eplugger.onekey.addModule;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.eplugger.onekey.entity.Category;
import com.eplugger.onekey.entity.Module;
import com.eplugger.onekey.entity.Modules;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import top.tobak.common.io.FileUtils;
import top.tobak.xml.dom4j.utils.ParseXmlUtils;

public class AddModuleMain {
	@Before
	public void testSetSchoolCode() {
		DBUtils.schoolInfo = SchoolInfo.广州番禺职业技术学院;
	}
	
	@Test
	public void testAddListModuleFun() throws Exception {
		AddModuleFun.AddListModuleFun();
		try {
			Desktop.getDesktop().open(new File(FileUtils.getUserHomeDirectory() + "AddListModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddMultiModuleFun() throws Exception {
		AddModuleFun.AddMultiModuleFun();
		try {
			Desktop.getDesktop().open(new File(FileUtils.getUserHomeDirectory() + "AddListModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddMultiModuleFun1() throws Exception {
		boolean authorSwitch = true;
		String template = "honor";
		AddModuleFun.AddMultipleModuleFun1(authorSwitch, template);
		try {
			Desktop.getDesktop().open(new File(FileUtils.getUserHomeDirectory() + "AddModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testName() throws Exception {
		Modules modules = ParseXmlUtils.toBean("src/main/resource/module/Module.xml", Modules.class);
		Module module = modules.getValidModule();
		List<Category> categories = modules.getCategories();
		System.out.println(categories);
		System.out.println(module);
		Gson gson = new Gson();
		String s = gson.toJson(module);
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println(s);
	}

	@Test
	public void testName1() {
		String str = "1";
		String str1 = "2";
		out: if (str.equals("1")) {
			System.out.println("if 1");
			if (str1.equals("1")) {
				break out;
			}
		} else if (str.equals("2")) {
			System.out.println("else if 2");
			if (str1.equals("2")) {
				break out;
			}
		} else {
			System.out.println("else");
		}
		System.out.println("end");
	}
}
