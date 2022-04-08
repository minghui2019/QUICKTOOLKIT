package com.eplugger.xml.dom4j.test;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jRead {
	public static void main(String[] args) throws DocumentException {
		// 1.得到decoument对象
		Document document = XmlFileUtils.readDocument();
		// 读取xml文件的信息
		// 2.读取根元素
		Element root = document.getRootElement();

		// 获取根下的第一个子元素
		Element element_1 = root.element("book");
		// 获取根下的第二个子元素
		Element element_2 = root.element("movie");
		// 获取book下的第一个子元素
		Element element_1_1 = element_1.element("name");
		// 获取movie下的第二个子元素
		Element element_2_2 = element_2.element("auther");

		// 通过迭代语句获取element所有book节点中的所有字节点
//		List<Element> elements = element_1.elements();
		Iterator<Element> itr = element_1.elementIterator();
		while (itr.hasNext()) {
			Element list = itr.next();
			System.out.println(list.getText());
		}

		// 3.获取元素下的文本信息
		String text1 = element_1_1.getText();
		String text2 = element_2_2.getText();
		System.out.println(text1);
		System.out.println(text2);
		// 获取属性的语句
		String id = element_1.attributeValue("id");
		System.out.println(id);
	}
}