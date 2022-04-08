package com.eplugger.xml.dom4j.test1;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Test1 {
	public static void main(String[] args) throws DocumentException {
		// 定义响应报文
		// 这里我直接使用构造方法（实际开发应以线程安全的单例模式）
		XmlUtil xmlUtil = new XmlUtil();
		Document document = XmlFileUtils.readDocument("src/main/java/com/eplugger/xml/dom4j/test1/test.xml", "UTF-8");
		Element rootElement = document.getRootElement();
		RspSchool rspSchool = new RspSchool();
		xmlUtil.xml2Bean(rootElement, rspSchool);
//		xmlUtil.parseXml(rootElement, "2", rspSchool);
		System.out.println(rspSchool);

	}
}
