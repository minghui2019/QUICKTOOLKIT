package com.eplugger.xml.dom4j.test;

import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jUpdate {
	public static void main(String[] args) throws FileNotFoundException, Exception {
		// 获取document
		Document document = XmlFileUtils.readDocument();
		// 获取根节点
		Element root = document.getRootElement();
		// 获取date元素
		Element element = root.element("book").element("date");
		// 更新它的数值
		element.setText("18/05/02");

		// 更改属性操作
		// 获取要更改属性的元素
		Element element1 = root.element("book");
		element1.addAttribute("id", "2");// 这里不是使用set方法而是add方法。

		// 调用回写函数
		XmlFileUtils.writeDocument(document);
	}
}
