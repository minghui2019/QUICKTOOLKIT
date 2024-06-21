package com.eplugger.util;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import top.tobak.xml.dom4j.util.XmlFileUtils;

/**
 * xml解析模版工具
 * @author minghui
 *
 * @param <T>
 */
@Deprecated
public class XmlParseUtils<T> {
	/** 具体的解析工具 */
	private IParseXml<T> utils;
	
	public XmlParseUtils() {
	}
	public XmlParseUtils(IParseXml<T> utils) {
		this.utils = utils;
	}
	
	/**
	 * 读取xml文件，并进行解析；utils实现类做具体解析
	 * @param filePath
	 * @return
	 */
	public List<T> parseXml(String filePath) {
		Document document = null;
		try {
			document = XmlFileUtils.readDocument(filePath);
		} catch (DocumentException e) {
			throw new RuntimeException(e + "文件打不开");
		}
        //3.获取根节点
        return utils.parseXml4Element2List(document.getRootElement());
	}
	
	/**
	 * 获取所有的List
	 * @param filePath
	 * @return
	 */
	public List<T> getAllList(String filePath) {
		return utils.parseAllList(this.parseXml(filePath));
	}
	
	/**
	 * 获取有效的List
	 * @param filePath
	 * @return
	 */
	public List<T> getValidList(String filePath) {
		return utils.parseValidList(this.getAllList(filePath));
	}
}
