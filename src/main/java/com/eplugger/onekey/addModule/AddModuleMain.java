package com.eplugger.onekey.addModule;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.eplugger.common.io.FileUtils;

public class AddModuleMain {
	public static void main(String[] args) {
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
