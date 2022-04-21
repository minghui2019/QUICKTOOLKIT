package com.eplugger.other.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.eplugger.xml.dom4j.util.XmlFileUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <pre>
 * XML解析工具类
 * 利用dom4j把xml解析为Map集合
 * </pre>
 */
public class XmlToMap {

	/**
	 * 将Document对象转为Map（String→Document→Map）
	 * 
	 * @param Document
	 * @return
	 */
	public static Map<String, Object> Dom2Map(Document doc) {
		if (doc == null) {
			return null;
		}
		Map<String, Object> map = Maps.newHashMap();
		Element root = doc.getRootElement();
		for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = iterator.next();
			if (e.elements().size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * 将Element对象转为Map（String→Document→Element→Map）
	 * 
	 * @param Element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> Dom2Map(Element e) {
		Map<String, Object> map = Maps.newHashMap();
		List<Element> list = e.elements();
		for (Element iter : list) {
			List<Object> mapList = Lists.newArrayList();
			Object val;
			if (iter.elements().size() > 0) {
				val = Dom2Map(iter);
			} else {
				val = iter.getText();
			}
			if (map.get(iter.getName()) != null) {
				Object obj = map.get(iter.getName());
				if (!obj.getClass().getName().equals("java.util.ArrayList")) {
					mapList = Lists.newArrayList();
					mapList.add(obj);
					mapList.add(val);
				}
				if (obj.getClass().getName().equals("java.util.ArrayList")) {
					mapList = (List<Object>) obj;
					mapList.add(val);
				}
				map.put(iter.getName(), mapList);
			} else {
				map.put(iter.getName(), val);// 公共map resultCode=0
			}
		}
		if (list.size() <= 0) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	public static void main(String[] args) throws DocumentException {
		Document document = XmlFileUtils.readDocument("src/main/resource/module/Module.xml");
		Map<String, Object> map = Dom2Map(document);
		System.out.println("map>>> " + map);
	}
}