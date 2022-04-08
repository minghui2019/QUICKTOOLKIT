package com.eplugger.xml.dom4j.test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocumentType;
import org.junit.Test;

import com.eplugger.annotation.dom4j.Dom4jAsAttribute;
import com.eplugger.util.ClassUtils;
import com.eplugger.xml.dom4j.test1.XmlUtil;
import com.eplugger.xml.dom4j.util.XmlFileUtils;

public class Dom4jTest {
	@Test
	public void testWriteXmlDocument() throws Exception {
		Dom4jTest d = new Dom4jTest();
		User user1 = new User(1, "姓名1", 18, "男");
		User user2 = new User(2, "姓名2", 19, "女");
		User user3 = new User(3, "石头", 20, "女");

		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);

		d.writeXmlDocument(User.class, users, "UTF-8", "src/main/java/com/eplugger/xml/dom4j/test/web.xml");
	}
	
	@Test
	public void testReadXML() throws Exception {
		Dom4jTest d = new Dom4jTest();
		List<User> readXML = d.readXML("src/main/java/com/eplugger/xml/dom4j/test/web.xml", User.class);
		System.out.println(readXML);
	}
	@Test
	public void testReadXML1() throws Exception {
		Dom4jTest d = new Dom4jTest();
//		Users readXML = d.readXML1("src/main/java/com/eplugger/xml/dom4j/test/web.xml", Users.class);
//		System.out.println(readXML);
		XmlUtil util = new XmlUtil();
		Document doc = XmlFileUtils.readDocument("src/main/java/com/eplugger/xml/dom4j/test/web.xml", "utf-8");
		Element createElement = DocumentHelper.createElement("UserList");
		Document document = DocumentHelper.createDocument(createElement);
		document.getRootElement().add((Element) doc.getRootElement().clone());
//		document.add((Element) doc.getRootElement().clone());
		Users users = new Users();
//		d.parseXml(document.getRootElement(), users);
		util.xml2Bean(document.getRootElement(), users);
		System.out.println(users);
	}

	public <T> void writeXmlDocument(Class<T> clazz, List<T> entityPropertys, String encoding, String XMLPathAndName) {
		long lasting = System.currentTimeMillis();// 效率检测

		try {
			File file = new File(XMLPathAndName);// 获得文件

			if (file.exists()) {
				file.delete();
			}
			// 新建student.xml文件并新增内容
			Document document = DocumentHelper.createDocument();
			document.setDocType(new DOMDocumentType("Users", null, "./web.dtd"));
			String rootname = clazz.getSimpleName();// 获得类名
			Element root = document.addElement(rootname + "s");// 添加根节点
			Field[] properties = clazz.getDeclaredFields();// 获得实体类的所有属性

			for (T t : entityPropertys) { // 递归实体
				Element secondRoot = root.addElement(rootname); // 二级节点
				for (Field field : properties) {
					if ("id".equals(field.getName())) {
						secondRoot.addAttribute(field.getName(), ClassUtils.getProperty(t, field.getName()).toString());
					} else {
						secondRoot.addElement(field.getName())
								.setText(ClassUtils.getProperty(t, field.getName()).toString());
					}

				}
				for (int i = 0; i < properties.length; i++) {
					// 为二级节点添加属性，属性值为对应属性的值
				}
			}

			XmlFileUtils.writeDocument(document, file, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("XML文件写入失败");
		}
		long lasting2 = System.currentTimeMillis();
		System.out.println("写入XML文件结束,用时" + (lasting2 - lasting) + "ms");
	}

	public <T> T readXML1(String XMLPathAndName, Class<T> clazz) {
		long lasting = System.currentTimeMillis();// 效率检测
		List<T> list = new ArrayList<T>();// 创建list集合
		try {
			Document doc = XmlFileUtils.readDocument(XMLPathAndName, "utf-8");
			Element root = doc.getRootElement();// 获得根节点
			Element foo;// 二级节点
			Field[] properties = clazz.getDeclaredFields();// 获得实例的属性

			for (Iterator<Element> i = root.elementIterator(clazz.getSimpleName()); i.hasNext();) {// 遍历t.getSimpleName()节点
				foo = i.next();// 下一个二级节点
				T entity = clazz.newInstance();// 获得对象的新的实例
				for (Field field : properties) {// 遍历所有孙子节点
					Object object = foo.elementText(field.getName());
					if (field.isAnnotationPresent(Dom4jAsAttribute.class)) {
						object = foo.attributeValue(field.getName());
					}
					if (field.getType() != String.class) {
						object = ConvertUtils.convert(object, field.getType());
					}
					ClassUtils.setProperty(entity, field.getName(), object, field.getType());
				}
				list.add(entity);
			}
			List<Element> elements = root.elements(clazz.getSimpleName());
			if (elements == null || elements.size() == 0) {
				System.out.println(root.getName());
			} else {
				
			}
			for (Element element : elements) {
				System.out.println(element.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long lasting2 = System.currentTimeMillis();
		System.out.println("读取XML文件结束,用时" + (lasting2 - lasting) + "ms");
		return null;
	}
	
	public void parseXml(Element root, Object obj) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//		List<Element> elements = root.elements(obj.getClass().getSimpleName());
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		if (root.getName().equals(obj.getClass().getSimpleName())) {
			for (Field field : declaredFields) {
				if (field != null && List.class.getName().equals(field.getType().getName())) {
					List<Object> temList = new ArrayList<Object>();
					ClassUtils.setProperty(obj, field.getName(), temList, List.class);
					Type genericType = field.getGenericType();
					if (genericType instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType) genericType;
						Type[] tArgs = pType.getActualTypeArguments();
						if (tArgs != null && tArgs.length > 0) {
							Field[] declaredFields2 = Class.forName(tArgs[0].getTypeName()).getDeclaredFields();
							List<Element> elements2 = root.elements();
							for (Element element : elements2) { //user
								Object tempObj = Class.forName(tArgs[0].getTypeName()).newInstance();
								for (Field field1 : declaredFields2) {
									Object object = element.elementText(field1.getName());
									if (field1.isAnnotationPresent(Dom4jAsAttribute.class)) {
										object = element.attributeValue(field1.getName());
									}
									if (field1.getType() != String.class) {
										object = ConvertUtils.convert(object, field1.getType());
									}
									ClassUtils.setProperty(tempObj, field1.getName(), object, field1.getType());
								}
								temList.add(tempObj);
							}
						}
					}
				} else {
					
				}
			}
		}
	}
	
	public <T> List<T> readXML(String XMLPathAndName, Class<T> clazz) {
		long lasting = System.currentTimeMillis();// 效率检测
		List<T> list = new ArrayList<T>();// 创建list集合
		try {
			Document doc = XmlFileUtils.readDocument(XMLPathAndName, "utf-8");
			Element root = doc.getRootElement();// 获得根节点
			Element foo;// 二级节点
			Field[] properties = clazz.getDeclaredFields();// 获得实例的属性
			
			for (Iterator<Element> i = root.elementIterator(clazz.getSimpleName()); i.hasNext();) {// 遍历t.getSimpleName()节点
				foo = i.next();// 下一个二级节点
				T entity = clazz.newInstance();// 获得对象的新的实例
				for (Field field : properties) {// 遍历所有孙子节点
					Object object = foo.elementText(field.getName());
					if (field.isAnnotationPresent(Dom4jAsAttribute.class)) {
						object = foo.attributeValue(field.getName());
					}
					if (field.getType() != String.class) {
						object = ConvertUtils.convert(object, field.getType());
					}
					ClassUtils.setProperty(entity, field.getName(), object, field.getType());
				}
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long lasting2 = System.currentTimeMillis();
		System.out.println("读取XML文件结束,用时" + (lasting2 - lasting) + "ms");
		return list;
	}
}
