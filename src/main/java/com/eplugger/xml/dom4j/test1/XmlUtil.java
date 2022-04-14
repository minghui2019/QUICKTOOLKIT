package com.eplugger.xml.dom4j.test1;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.eplugger.util.ClassUtils;
import com.eplugger.util.StringUtils;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class XmlUtil {
	private static Log log = LogFactory.get();
	// ${abc}正则
	public static String varRegex = "\\$\\{\\s*(\\w+)\\s*(([\\+\\-])\\s*(\\d+)\\s*)?\\}";

	/**
	 * xml解析成document对象
	 *
	 * @param xml
	 * @return
	 */
	public Document getDocument(String xml) {
		StringReader stringReader = new StringReader(xml);
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(stringReader);
		} catch (DocumentException e) {

		}
		return document;
	}

	/**
	 * xml与bean的相互转换
	 *
	 * @param element
	 * @param direction 1：java2xml，2：xml2java
	 * @param obj
	 */
	public void xml2Bean(Element element, Object obj) {
		// 获取当前元素的所有子节点（在此我传入根元素）
		List<Element> elements = element.elements();
		// 判断是否有子节点
		if (elements != null && elements.size() > 0) {// 进入if说明有子节点
			for (Element e : elements) { // 遍历
				// 声明Field
				Field field = null;
				try {
					// 反射获取属性
					field = obj.getClass().getDeclaredField(StringUtils.firstCharLowerCase(e.getName()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// 获取当前属性是否为list
				if (field != null && List.class.getName().equals(field.getType().getName())) {
					List<Object> temList = new ArrayList<Object>();
					ClassUtils.setProperty(obj, e.getName(), temList, List.class);
					
					// 获取List的泛型参数类型
					Type gType = field.getGenericType();
					// 判断当前类型是否为参数化泛型
					if (gType instanceof ParameterizedType) {
						// 转换成ParameterizedType对象
						ParameterizedType pType = (ParameterizedType) gType;
						// 获得泛型类型的泛型参数（实际类型参数)
						Type[] tArgs = pType.getActualTypeArguments();
						if (tArgs != null && tArgs.length > 0) {
							// 获取当前元素的所有子元素
							List<Element> elementSubList = e.elements();
							// 遍历
							for (Element e1 : elementSubList) {
								try {
									// 反射创建对象
									Object tempObj = Class.forName(tArgs[0].getTypeName()).newInstance();
									temList.add(tempObj);
									// 递归调用自身
									this.xml2Bean(e1, tempObj);
								} catch (Exception e2) {
									log.error("【{}】对象构造失败", tArgs[0].getTypeName(), e2);
								}
							}
						}
					}
				} else { // 说明不是list标签，继续递归调用自身即可
					this.xml2Bean(e, obj);
				}
				// 此时还在for循环遍历根元素的所有子元素
			}
		} else { // 说明无子节点
			// 获取当前元素的名称
			String nodeName = element.getName();
			// 获取当前元素的对应的值
			String nodeValue = element.getStringValue();
			if (StringUtils.isNotBlank(nodeName)) {
				ClassUtils.setProperty(obj, nodeName, nodeValue, String.class);
			}
		}
	}
	public void xml2Bean1(Element element, Object obj) {
		// 获取当前元素的所有子节点（在此我传入根元素）
		List<Element> elements = element.elements();
		System.out.println(element.getName() + ": " + element.getStringValue());
		// 判断是否有子节点
		if (elements != null && elements.size() > 0) {// 进入if说明有子节点
			for (Element e : elements) { // 遍历
				System.out.println(e.getName() + ": " + e.getStringValue());
				// 声明Field
				Field field = null;
				try {
					// 反射获取属性
					field = obj.getClass().getDeclaredField(e.getName());
				} catch (Exception e1) {
				}
				// 获取当前属性是否为list
				if (field != null && List.class.getName().equals(field.getType().getName())) {
					// 获取List的泛型参数类型
					Type gType = field.getGenericType();
					// 判断当前类型是否为参数化泛型
					if (gType instanceof ParameterizedType) {
						// 转换成ParameterizedType对象
						ParameterizedType pType = (ParameterizedType) gType;
						// 获得泛型类型的泛型参数（实际类型参数)
						Type[] tArgs = pType.getActualTypeArguments();
						if (tArgs != null && tArgs.length > 0) {
							// 获取当前元素的所有子元素
							List<Element> elementSubList = e.elements();
							// 遍历
							for (Element e1 : elementSubList) {
								try {
									// 反射创建对象
									Object tempObj = Class.forName(tArgs[0].getTypeName()).newInstance();
									// 递归调用自身
									this.xml2Bean1(e1, tempObj);
								} catch (Exception e2) {
									log.error("【{}】对象构造失败", tArgs[0].getTypeName(), e2);
								}
							}
						}
					}
				} else { // 说明不是list标签，继续递归调用自身即可
					this.xml2Bean1(e, obj);
				}
				// 此时还在for循环遍历根元素的所有子元素
			}
		} else { // 说明无子节点
		}
	}
	
	public void parseXml(Element element, String direction, Object obj) {
		// 获取当前元素的所有子节点（在此我传入根元素）
		List<Element> elements = element.elements();
		// 判断是否有子节点
		if (elements != null && elements.size() > 0) {
			// 进入if说明有子节点
			// 遍历
			for (Element e : elements) {
				// 判断转换方向（1：java2xml；2：xml2java）
				if ("2".equals(direction)) {// 这里是xml转bean
					// 声明Field
					Field field = null;
					try {
						// 反射获取属性
						field = obj.getClass().getDeclaredField(e.getName());
					} catch (Exception e1) {
					}
					// 获取当前属性是否为list
					if (field != null && List.class.getName().equals(field.getType().getName())) {
						List<Object> temList = new ArrayList<Object>();
						ClassUtils.setProperty(obj, e.getName(), temList, List.class);
						
						// 获取List的泛型参数类型
						Type gType = field.getGenericType();
						// 判断当前类型是否为参数化泛型
						if (gType instanceof ParameterizedType) {
							// 转换成ParameterizedType对象
							ParameterizedType pType = (ParameterizedType) gType;
							// 获得泛型类型的泛型参数（实际类型参数)
							Type[] tArgs = pType.getActualTypeArguments();
							if (tArgs != null && tArgs.length > 0) {
								// 获取当前元素的所有子元素
								List<Element> elementSubList = e.elements();
								// 遍历
								for (Element e1 : elementSubList) {
									try {
										// 反射创建对象
										Object tempObj = Class.forName(tArgs[0].getTypeName()).newInstance();
										temList.add(tempObj);
										// 递归调用自身
										this.parseXml(e1, direction, tempObj);
									} catch (Exception e2) {
										log.error("【{}】对象构造失败", tArgs[0].getTypeName(), e2);
									}
								}
							}
						}
					} else {
						// 说明不是list标签，继续递归调用自身即可
						this.parseXml(e, direction, obj);
					}
				} else if ("1".equals(direction)) {// 说明转换方向为：javabean转xml
					// 递归调用自身
					this.parseXml(e, direction, obj);
				}
				// 此时还在for循环遍历根元素的所有子元素
			}
		} else {
			// 说明无子节点
			// 获取当前元素的名称
			String nodeName = element.getName();
			// 获取当前元素的对应的值
			String nodeValue = element.getStringValue();

			// 判断转换方向：1：java2xml、2：xml2java
			if ("1".equals(direction)) {// java2xml
				if (nodeValue != null && nodeValue.matches(varRegex)) {
					/**
					 * 获取模板中各节点定义的变量名，例如<traceNo>${traceNo}</traceNo>
					 */
					nodeValue = nodeValue.substring(nodeValue.indexOf("${") + 2, nodeValue.indexOf("}"));

					Object value = ClassUtils.getProperty(obj, nodeValue);
					// 将变量值填充至xml模板变量名位置，例如<traceNo>${traceNo}</traceNo>
					element.setText(value == null ? "" : value.toString());
				}
				// 叶子节点
				log.debug("节点名【{}】，节点变量名【{}】", element.getName(), nodeValue);
			} else if ("2".equals(direction)) {// xml2java
				if (nodeName != null && !"".equals(nodeName)) {
					// 根据xml节点名，调用obj对象的setXXX()方法为obj设置变量值
					ClassUtils.setProperty(obj, nodeName, nodeValue, String.class);
				}
			}
		}
	}
}