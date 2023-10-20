package com.eplugger.xml.dom4j.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import top.tobak.xml.dom4j.util.XmlFileUtils;

public class TestDom4JOperation {
	private static Document readDocument() throws DocumentException {
		String xmlPath = TestDom4JOperation.class.getResource("/book.xml").getFile();
		assertNotNull(xmlPath);
		return XmlFileUtils.readDocument(new File(xmlPath), "UTF-8");
	}
	
	private static String writeDocument(Document document) throws IOException {
		String xmlPath = TestDom4JOperation.class.getResource("/").getPath() + "/book_out.xml";
		File file = new File(xmlPath);
		XmlFileUtils.writeDocument(document, file, "UTF-8");
		return file.getCanonicalPath();
	}
	
	/**
	 * 从xml文件获取节点信息
	 * @throws Exception
	 */
	@Test
	public void testReadElement() throws Exception {
		// 1.得到decoument对象
		Document document = TestDom4JOperation.readDocument();
		// 读取xml文件的信息
		// 2.读取根元素
		Element root = document.getRootElement();

		// 通过指定的节点名，获取根下的第一个子元素
		Element element_1 = root.element("book");
		// 获取book下的第一个子元素
		Element element_1_1 = element_1.element("name");
		// 3.获取元素下的文本信息
		String text1 = element_1_1.getText();
		System.out.println(element_1_1.getName() + " = " + text1);
		// 获取属性的语句
		String id = element_1.attributeValue("id");
		System.out.println("id = " + id);

		// 通过迭代语句获取element所有book节点中的所有节点
//		List<Element> elements = element_1.elements();
		for (Iterator<Element> itr = element_1.elementIterator(); itr.hasNext(); ) {
			Element element = itr.next();
			for (Iterator<Attribute> attrItor = element.attributeIterator(); attrItor.hasNext(); ) {
				Attribute attr = attrItor.next();
				System.out.println(attr.getName() + " = " + attr.getText());
			}
			System.out.println(element.getName() + " = " + element.getText());
		}
	}
	
	/**
	 * 把字符串形式的xml格式文本格式化为xml文件
	 * @throws Exception
	 */
	@Test
	public void testParseText() throws Exception {
		String str = "<movie><name>杨瑞琦</name><auther>余飞</auther></movie>";// 要存入的内容
		// 得到document
		Document document = TestDom4JOperation.readDocument();
		// 获得根节点
		Element element = document.getRootElement();
		// 将字符串转换为xml文件
		Document doc = DocumentHelper.parseText(str);
		// 因为xml文件还有自己的标准结构文件，所以转换后的文件还有标准结构文件
		// 解决方法：获取这个xml文件的头节点，就是我们需要添加的内容
		element.add(doc.getRootElement());
		// 调用回写函数
		TestDom4JOperation.writeDocument(document);
	}
	
	/**
	 * 往现有节点里插入新节点
	 * @throws Exception
	 */
	@Test
	public void testAddElement() throws Exception {
		// 1.得到document
		Document document = TestDom4JOperation.readDocument();
		// 2.得到要添加到某个元素里面的位置
		Element element = document.getRootElement().element("book");
		// 3.创建子元素
		Element date = DocumentHelper.createElement("date");
		// 增加内容
		date.addText("2018/5/2");
		// 增加属性
		date.addAttribute("ch", "man");
		// 直接插到当前节点内的最后一个节点后，无法指定插入的位置
//		element.add(date);
		// 创建子元素并且可以选择插入位置
		List<Element> list = element.elements();
		list.add(1, date);

		// 4.回写
		String fileName = TestDom4JOperation.writeDocument(document);
		System.out.println(fileName);
	}
	
	/**
	 * 更新节点的值
	 * @throws Exception
	 */
	@Test
	public void testUpdateElement() throws Exception {
		// 获取document
		Document document = TestDom4JOperation.readDocument();
		// 获取根节点
		Element root = document.getRootElement();
		// 获取date元素
		Element element = root.element("book").element("date");
		// 更新它的数值
		element.setText("22/04/19");

		// 更改属性操作
		// 获取要更改属性的元素
		Element element1 = root.element("book");
		element1.addAttribute("id", "9");// 这里不是使用set方法而是add方法。

		// 调用回写函数
		TestDom4JOperation.writeDocument(document);
	}
	
	/**
	 * 移除某个节点
	 * @throws Exception
	 */
	@Test
	public void testRemoveElement() throws Exception {
		// 1.获取document
		Document document = TestDom4JOperation.readDocument();
		// 1).先找到需要删除的某个节点，必须获得他的父节点才能删除
		Element element = document.getRootElement().element("book").element("date");
		// 2).必须获得它的父节点才能删除，不能自己删除自己
		element.getParent().remove(element);
		// 3).删除元素的属性操作
		element = document.getRootElement().element("book");
		element.remove(element.attribute("id"));
		// 调用回写函数
		TestDom4JOperation.writeDocument(document);
	}
}
