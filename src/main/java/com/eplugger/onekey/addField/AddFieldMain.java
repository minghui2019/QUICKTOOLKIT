package com.eplugger.onekey.addField;

import org.junit.Test;

import com.eplugger.trans.TextTrans;

public class AddFieldMain {
	@Test
	public void testCreateFieldXml() throws Exception {
		TextTrans.createFieldXml("主项目、项目类型、项目介绍、分管副院长");
	}
	
	@Test
	public void testCreateSqlAndJavaFile() throws Exception {
		AddFieldFun.createSqlAndJavaFile();
	}
}
