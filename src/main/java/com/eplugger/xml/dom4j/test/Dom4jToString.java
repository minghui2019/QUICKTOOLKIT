package com.eplugger.xml.dom4j.test;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jToString {

	public static void main(String[] args) throws Exception {
		String str = " <movie><name>杨瑞琦</name><auther>余飞</auther></movie> ";// 要存入的内容
		// 得到document
		Document document = XmlFileUtils.readDocument();
		// 获得根节点
		Element element = document.getRootElement();
		// 将字符串转换为xml文件
		Document doc = DocumentHelper.parseText(str);
		// 因为xml文件还有自己的标准结构文件，所以转换后的文件还有标准结构文件
		// 解决方法：获取这个xml文件的头节点，就是我们需要添加的内容
		element.add(doc.getRootElement());
		// 调用回写函数
		XmlFileUtils.writeDocument(document);
	}
}