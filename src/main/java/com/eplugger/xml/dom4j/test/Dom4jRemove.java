package com.eplugger.xml.dom4j.test;

import org.dom4j.Document;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jRemove {
	public static void main(String[] args) throws Exception {
		// 1.获取document
		Document document = XmlFileUtils.readDocument();
		/* （1）。删除某个节点必须获得他的父节点才能删除，不能自己删除自己 */
		Element element = document.getRootElement().element("book").element("date");
		// 得到父节点并删除它
		element.getParent().remove(element);
		/* （2）。删除元素的属性操作 */
		element.remove(element.attribute("ch"));
		// 调用回写函数
		XmlFileUtils.writeDocument(document);
	}
}