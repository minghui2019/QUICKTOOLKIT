package com.eplugger.xml.dom4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlFileUtils {
	private XmlFileUtils() {
	}
	
	public static Document readDocument(String filePath) throws DocumentException {
		return XmlFileUtils.readDocument(new File(filePath), "UTF-8");
	}
	
	/**
	 * 读取xml文件
	 * @param filePath 文件路径
	 * @return org.dom4j.Document
	 * @throws DocumentException
	 */
	public static Document readDocument(String filePath, String encoding) throws DocumentException {
        return XmlFileUtils.readDocument(new File(filePath), encoding);
	}
	
	public static Document readDocument(File file, String encoding) throws DocumentException {
		//1.创建Reader对象
		SAXReader reader = new SAXReader();
		reader.setEncoding(encoding);
		//2.加载xml
		return reader.read(file);
	}
	
	public static void writeDocument(Document document, String uri, String encoding) throws IOException {
		/* 这种方法可以格式化xml且解决字符乱码问题 */
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		format.setIndentSize(4);
		XMLWriter writer = new XMLWriter(new FileOutputStream(uri), format);
		writer.write(document);
		writer.flush();
		writer.close();
	}
	
	public static void writeDocument(Document document, File file, String encoding) throws IOException {
		/* 这种方法可以格式化xml且解决字符乱码问题 */
		// 设置XML文件的编码格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		format.setIndentSize(4);
		// 声明写XML的对象
		XMLWriter writer = new XMLWriter(new FileWriter(file), format);
		// 生成XML文件
		writer.write(document);
		writer.flush();
		writer.close();
	}
}
