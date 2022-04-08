package com.eplugger.xml.dom4j.test;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jAdd {
	public static void main(String[] args) throws DocumentException, Exception {
		// 1.得到document
		Document document = XmlFileUtils.readDocument();
		// 2.得到要添加到某个元素里面的位置
		Element element = document.getRootElement().element("book");

		// 3.创建子元素
		Element date = DocumentHelper.createElement("date");
		// 增加内容
		date.addText("2018/5/2");
		// 增加属性
		date.addAttribute("ch", "man");
		// element.add(date);（这个无法指定插入的位置）
		// 创建子元素并且可以选择插入位置
		List<Element> list = element.elements();
		list.add(1, date);

		// 4.回写
		XmlFileUtils.writeDocument(document);
	}
}