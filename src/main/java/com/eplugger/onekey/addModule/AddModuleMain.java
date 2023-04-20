package com.eplugger.onekey.addModule;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.eplugger.common.io.FileUtils;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import com.eplugger.utils.DBUtils;
import org.junit.Before;
import org.junit.Test;

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
		boolean authorSwitch = false;
		String template = "honor";
		AddModuleFun.AddMultipleModuleFun1(authorSwitch, template);
		try {
			Desktop.getDesktop().open(new File(FileUtils.getUserHomeDirectory() + "AddModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
