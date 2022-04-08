package com.eplugger.dom4j;
 
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;

import com.eplugger.dom4j.annotation.Dom4jFieldXml;
import com.eplugger.dom4j.annotation.Dom4jXml;
import com.eplugger.dom4j.model.Dom4jXmlAttribute;
import com.eplugger.dom4j.model.Dom4jXmlDemo;
import com.eplugger.dom4j.model.Dom4jXmlField;
import com.eplugger.dom4j.model.Dom4jXmlNamespace;

import cn.hutool.core.io.FileUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
 
/**
 * Dom4jXmlUtils
 *
 * @author: hh
 * @since: 2021/7/6 1:32
 */
@SuppressWarnings("restriction")
public class Dom4jXmlUtils {
    private Dom4jXmlUtils() {
        throw new IllegalArgumentException("工具类");
    }
 
    public static <T> T xmlToBean(String xmlStr, Class<T> tClass) throws Exception {
        if (xmlStr == null || "".equals(xmlStr) || tClass == null) {
            return null;
        }
        //是否标注可转换
        if (!tClass.isAnnotationPresent(Dom4jXml.class)) {
            return null;
        }
        T t = null;
        //获取注解
        Dom4jXml dom4jXml = tClass.getAnnotation(Dom4jXml.class);
        Document doc = DocumentHelper.parseText(xmlStr);
        Element rootElement = doc.getRootElement();
        // 根属性转换
        Class<?> rootAttributeClass = dom4jXml.namespace();
        Object rootAttribute = null;
        if (rootAttributeClass != void.class) {
            //属性转换的对象
            rootAttribute = readNamespace(rootElement, rootAttributeClass);
        }
        //节点处理
        t = createInstanceAndField(rootElement, tClass);
        try {
            Field field = tClass.getDeclaredField("namespace");
            field.setAccessible(true);
            field.set(t, rootAttribute);
        } catch (NoSuchFieldException exception) {
            //无根
        }
        return t;
    }
 
 
    public static <T> T createInstanceAndField(Element element, Class<T> tClass) throws Exception {
        T t = tClass.newInstance();
 
        List<Field> allField = getAllField(tClass);
        for (Field field : allField) {
            String name = field.getName();
            boolean nameFlag = false;
            boolean attributeFlag = false;
            Dom4jFieldXml dom4jFieldXml = field.getAnnotation(Dom4jFieldXml.class);
            if (dom4jFieldXml != null && !"".equals(dom4jFieldXml.name())) {
                nameFlag = true;
            }
            if (dom4jFieldXml != null && dom4jFieldXml.attribute() != void.class) {
                attributeFlag = true;
            }
            if (field.getType() == List.class) {
                List<Element> elementList = element.elements(name);
                if (nameFlag) {
                    elementList = element.elements(dom4jFieldXml.name());
                }
                if (elementList == null || elementList.isEmpty()) {
                    continue;
                }
 
                List<Object> fieldValue = new ArrayList<Object>();
                List<Object> attributeValue = new ArrayList<Object>();
                for (Element ele : elementList) {
                    if (attributeFlag) {
                        Object nodeAttribute = readAttribute(ele, dom4jFieldXml.attribute());
                        attributeValue.add(nodeAttribute);
                    }
 
                    if (ele.elements().isEmpty()) {
                        fieldValue.add(ele.getData());
                        continue;
                    }
					Class<?> eClass = (Class<?>) ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments()[0];
                    Object nextValue = createInstanceAndField(ele, eClass);
                    fieldValue.add(nextValue);
                }
                Field declaredField = tClass.getDeclaredField(name + "Attribute");
                declaredField.setAccessible(true);
                declaredField.set(t, attributeValue);
 
                field.setAccessible(true);
                field.set(t, fieldValue);
                continue;
            }
            //节点
            Element nodeElement = element.element(name);
            if (nameFlag) {
                nodeElement = element.element(dom4jFieldXml.name());
            }
            if (nodeElement == null) {
                continue;
            }
            //设置根属性
            if (attributeFlag) {
                Object nodeAttribute = readAttribute(nodeElement, dom4jFieldXml.attribute());
                Field declaredField = tClass.getDeclaredField(name + "Attribute");
                declaredField.setAccessible(true);
                declaredField.set(t, nodeAttribute);
            }
 
            //设置元素值
            if (nodeElement.elements().isEmpty()) {
                field.setAccessible(true);
                field.set(t, nodeElement.getData());
                continue;
            }
            Object fieldValue = createInstanceAndField(nodeElement, field.getType());
            field.setAccessible(true);
            field.set(t, fieldValue);
        }
        return t;
    }
 
    private static <T> T readNamespace(Element element, Class<T> tClass) throws Exception {
        T t = tClass.newInstance();
        List<Field> allFieldList = getAllField(tClass);
        for (Field field : allFieldList) {
            Namespace namespace = element.getNamespaceForPrefix(field.getName());
 
            Dom4jFieldXml dom4jFieldXml = field.getAnnotation(Dom4jFieldXml.class);
            if (dom4jFieldXml != null) {
                String name = dom4jFieldXml.name();
                namespace = element.getNamespaceForPrefix(name);
            }
            if (namespace != null) {
                //设置属性
                field.setAccessible(true);
                field.set(t, namespace.getURI());
            }
        }
        return t;
    }
 
    /**
     * 属性对象处理
     *
     * @param element 属性节点
     * @param tClass  对象
     * @param <T>     属性类型
     * @return 属性对象
     * @throws Exception 异常
     */
    private static <T> T readAttribute(Element element, Class<T> tClass) throws Exception {
        //获取属性
        List<Field> allFieldList = getAllField(tClass);
 
        Map<String, String> allAttributeMap = getAllAttribute(element);
 
        T t = tClass.newInstance();
        for (Field field : allFieldList) {
            //原属性
            String value = allAttributeMap.get(field.getName());
 
            Dom4jFieldXml dom4jFieldXml = field.getAnnotation(Dom4jFieldXml.class);
            if (dom4jFieldXml != null) {
                String name = dom4jFieldXml.name();
                if (!"".equals(name)) {
                    value = allAttributeMap.get(name);
                }
            }
            if (value != null) {
                //设置属性
                field.setAccessible(true);
                field.set(t, value);
            }
 
        }
        return t;
    }
 
    private static Map<String, String> getAllAttribute(Element element) {
        Map<String, String> map = new HashMap<>(element.attributeCount());
        //属性map
        for (Iterator<?> it = element.attributeIterator(); it.hasNext(); ) {
            Attribute attribute = (Attribute) it.next();
            String name = attribute.getName();
            String text = attribute.getValue();
            map.put(name, text);
        }
        return map;
    }
 
    private static List<Field> getAllField(Class<?> tClass) {
        Class<?> clazz = tClass;
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
 
    public static <T> String beanToXml(T t) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (t == null) {
            throw new NullPointerException("对象为空");
        }
        Document document = DocumentHelper.createDocument();
        Class<?> tClass = t.getClass();
        //是否标注可转换
        if (!tClass.isAnnotationPresent(Dom4jXml.class)) {
            return "";
        }
        //获取注解
        Dom4jXml dom4jXml = tClass.getAnnotation(Dom4jXml.class);
        String name = dom4jXml.name();
        Element rootElement;
        if ("".equals(name)) {
            rootElement = document.addElement("root");
        } else {
            rootElement = document.addElement(name);
        }
        //命名空间处理
        Class<?> namespaceClass = dom4jXml.namespace();
        if (namespaceClass != void.class) {
            Field namespaceField = tClass.getDeclaredField("namespace");
            //命名空间 对象
            Object nameSpaceValue = getFieldValue(t, namespaceField);
            //所有字段
            List<Field> nameSpaceField = getAllField(namespaceClass);
            for (Field field : nameSpaceField) {
                Object fieldValue = getFieldValue(nameSpaceValue, field);
                if (fieldValue == null) {
                    continue;
                }
                Dom4jFieldXml dom4jFieldXml = field.getAnnotation(Dom4jFieldXml.class);
                String url = String.valueOf(fieldValue);
                if (dom4jFieldXml == null) {
                    rootElement.add(new Namespace(field.getName(), url));
                    continue;
                }
                rootElement.add(new Namespace(dom4jFieldXml.name(), url));
            }
        }
        writeElement(t, tClass, rootElement);
 
        return document.asXML();
    }
 
    private static boolean isBaseType(Object tClass) {
        if ((tClass instanceof Number) ||
                (tClass instanceof Boolean) ||
                (tClass instanceof String) ||
                (tClass instanceof Date)) {
            return true;
        }
        return false;
    }
 
    private static <T> Object getFieldAttributeValue(T t, Field field) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> tClass = t.getClass();
        String methodName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method m = tClass.getMethod("get" + methodName + "Attribute");
        return m.invoke(t);
    }
 
    private static void writeElement(Object t, Class<?> tClass, Element rootElement) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Field> allField = getAllField(tClass);
        for (Field field : allField) {
            if (field.getName().endsWith("Attribute") || "namespace".equals(field.getName())) {
                continue;
            }
            Object fieldValue = getFieldValue(t, field);
            if (fieldValue == null) {
                continue;
            }
 
            // 存在字段注解
            Dom4jFieldXml dom4jFieldXml = field.getAnnotation(Dom4jFieldXml.class);
            String elementName = field.getName();
            if (dom4jFieldXml != null && !"".equals(dom4jFieldXml.name())) {
                elementName = dom4jFieldXml.name();
            }
            boolean attributeFlag = false;
            Object attributeValue = null;
            Field declaredField = null;
 
            if (dom4jFieldXml != null && (dom4jFieldXml.attribute()) != void.class) {
                attributeFlag = true;
                attributeValue = getFieldAttributeValue(t, field);
                declaredField = tClass.getDeclaredField(field.getName() + "Attribute");
            }
 
            //list对象
            if (field.getType() == List.class) {
                List<?> list = (List<?>) fieldValue;
                List<?> aList = null;
                if (attributeFlag) {
                    aList = (List<?>) attributeValue;
                }
                for (int i = 0; i < list.size(); i++) {
                    Element element = rootElement.addElement(elementName);
                    Object fVal = list.get(i);
                    if (fVal == null) {
                        continue;
                    }
                    if (isBaseType(fVal)) {
                        element.setText(String.valueOf(fVal));
                    } else {
                        writeElement(fVal, fVal.getClass(), element);
                    }
 
                    if (attributeFlag) {
                        if (aList == null) {
                            continue;
                        }
                        Object aVal = aList.get(i);
                        writeAttribute(aVal, declaredField, element);
                    }
                }
                continue;
            }
            Element element = rootElement.addElement(elementName);
            if (isBaseType(fieldValue)) {
                element.setText(String.valueOf(fieldValue));
            } else {
                writeElement(fieldValue, fieldValue.getClass(), element);
            }
            //属性设置
            if (attributeFlag) {
                if (attributeValue == null) {
                    continue;
                }
                writeAttribute(attributeValue, declaredField, element);
            }
        }
 
    }
 
	private static void writeAttribute(Object fieldValue, Field field, Element element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> type = field.getType();
        if (type == List.class) {
            type = (Class<?>) ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments()[0];
        }
        List<Field> allField = getAllField(type);
        for (Field fd : allField) {
            Dom4jFieldXml dom4jFieldXml = fd.getAnnotation(Dom4jFieldXml.class);
            Object fdValue = getFieldValue(fieldValue, fd);
            if (fdValue == null) {
                continue;
            }
            String name;
            if (dom4jFieldXml != null && "".equals(dom4jFieldXml.name())) {
                name = dom4jFieldXml.name();
            } else {
                name = fd.getName();
            }
            element.addAttribute(name, String.valueOf(fdValue));
        }
    }
 
    private static <T> Object getFieldValue(T t, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> tClass = t.getClass();
        String methodName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method m = tClass.getMethod("get" + methodName);
        return m.invoke(t);
    }
 
    public static void main(String[] args) throws Exception {
        String str = FileUtil.readString(new File("src/test/java/test.xml"), "utf-8");
        Dom4jXmlDemo ww = xmlToBean(str, Dom4jXmlDemo.class);
        System.out.println("gg");
        System.out.println(ww);
 
 
        Dom4jXmlDemo dom4jXmlDemo = new Dom4jXmlDemo();
        Dom4jXmlNamespace namespace = new Dom4jXmlNamespace();
        namespace.setA("urn:gsma:params:xml:ns:rcs:rcs:fthttp");
        namespace.setB("urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext");
        dom4jXmlDemo.setNamespace(namespace);
        Dom4jXmlField dom4jXmlField = new Dom4jXmlField();
        dom4jXmlField.setHh("sg");
        dom4jXmlField.setSh("sb");
 
        List<Dom4jXmlField> list = new ArrayList<>();
        list.add(dom4jXmlField);
 
        Dom4jXmlAttribute dom4jXmlAttribute = new Dom4jXmlAttribute();
        dom4jXmlAttribute.setKs("zoule");
        dom4jXmlAttribute.setWs("laile");
        List<Dom4jXmlAttribute> list2 = new ArrayList<>();
        list2.add(dom4jXmlAttribute);
 
        dom4jXmlDemo.setIdList(list);
        dom4jXmlDemo.setIdListAttribute(list2);
 
        System.out.println(beanToXml(dom4jXmlDemo));
    }
}