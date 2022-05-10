package com.eplugger.onekey.addModule;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.eplugger.onekey.entity.Field;

public class AddModuleMain {
	public static void main(String[] args) {
//		AddModuleFun.AddSingleModuleFun();
//		AddModuleFun.AddMultipleModuleFun();
//		List<Module> module = ModuleParse.getInstance().getValidList("src/main/resource/module/Module.xml");
//		Module module = ModuleParse.getInstance().getValidModule("src/resource/module/Module.xml");
//		System.out.println(module);
		List<Field> fieldList = Constants.getFieldList();
		System.out.println(fieldList);
		
	}
	
	@Test
	public void testAddListModuleFun() throws Exception {
		AddModuleFun.AddListModuleFun();
		try {
			Desktop.getDesktop().open(new File("C:/Users/Admin/Desktop/AddListModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddMultiModuleFun() throws Exception {
		AddModuleFun.AddMultiModuleFun();
		try {
			Desktop.getDesktop().open(new File("C:/Users/Admin/Desktop/AddListModule"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
