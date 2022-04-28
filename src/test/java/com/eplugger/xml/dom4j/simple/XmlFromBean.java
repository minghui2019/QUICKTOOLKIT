package com.eplugger.xml.dom4j.simple;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.commons.collections.CollectionUtils;
import com.eplugger.commons.lang3.reflect.FieldUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.eplugger.xml.dom4j.util.XmlFileUtils;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlFromBean {
	/**
	 * XML 文件路径
	 */
	private final String path;

	/**
	 * 文件编码, 默认使用 UTF-8
	 */
	private final String encoding;

	public XmlFromBean(String path) {
		this(path, "UTF-8");
	}

	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param fileEncoding 文件编码
	 */
	public XmlFromBean(String path, Charset fileEncoding) {
		this(path, fileEncoding.name());
	}
	
	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param encoding 文件编码
	 */
	public XmlFromBean(String path, String encoding) {
		this.path = path;
		this.encoding = encoding;
	}

	/**
	 * 把Document对象写到文件中
	 * @param document
	 */
	private void writeDocument(Document document) {
		try {
			XmlFileUtils.writeDocument(document, new File(path), encoding);
		} catch (IOException e) {
			log.error("xml文件写出失败, 请检查路径[path=" + path + "]" + e.getMessage());
		}
	}
	
	/**
     * 指定对象转换为Document对象, 目标对象必须使用{@link Dom4JTag @Dom4JTag}注解
     * 默认不写出文件
     *
     * @param data 目标对象
     * @param      <T> 对象类型
     * @return Document实体, 目标对象为null时总是返回null
     */
	public <T> Document fromBean(T data) {
		return fromBean(data, false);
	}
	
	/**
     * 指定对象转换为Document对象, 目标对象必须使用{@link Dom4JTag @Dom4JTag}注解
     *
     * @param data 目标对象
     * @param isAutoWrite2File 是否写出到文件
     * @param      <T> 对象类型
     * @return Document实体, 目标对象为null时总是返回null
     */
	public <T> Document fromBean(T data, boolean isAutoWrite2File) {
		Element fromBean = fromBean1(data);
		Document document = DocumentHelper.createDocument(fromBean);
		if (isAutoWrite2File)
			writeDocument(document);
		return document;
	}
	
	/**
     * 指定对象转换为Element对象, 目标对象必须使用{@link Dom4JTag @Dom4JTag}注解
     *
     * @param data 目标对象
     * @param      <T> 对象类型
     * @return Element实体, 目标对象为null时总是返回null
     */
	private <T> Element fromBean1(T data) {
		if (data == null)
			return null;

		Class<?> type = data.getClass();
		Dom4JTag xmlTag = type.getAnnotation(Dom4JTag.class);
		if (null == xmlTag)
			throw new UnsupportedOperationException("实体类[" + type.toString() + "]没有被标记为 @Dom4JTag");

		// 一级属性
		String tagName = getTargetTagName(xmlTag, type.getSimpleName());
		Element root = DocumentHelper.createElement(tagName);
		List<Field> fields = FieldUtils.getAllFieldsList(type);
		for (Field field : fields) {
			// 不处理静态和常量
			if (FieldUtils.isStaticOrFinal(field)) {
				log.error("实体类[" + type.toString() + "]的字段[" + field.getName() + "]为final或static");
				continue;
			}

			// 获取字段相关数据
			// @Dom4JField注解
			Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
			if (xmlField == null) {
                log.error("字段[" + field.getName() + "]没有被标记为 @Dom4JField");
                continue;
            }

			// 字段XML映射名称
			Class<?> fieldType = field.getType();
			String fieldName = getTargetTagName(xmlField, field.getName());

			// 获取字段值
            Object fieldValue = FieldUtils.getFieldValue(data, field);
            
            Dom4JFieldType dom4jFieldType = xmlField.type();
            if (Dom4JFieldType.ATTRIBUTE == dom4jFieldType) {
            	getValueByAttribute(root, fieldType, fieldName, fieldValue);
            }

            if (Dom4JFieldType.TAG == dom4jFieldType) {
            	getValueByTag(root, field, fieldName, fieldValue);
            }

            if (Dom4JFieldType.ELEMENT == dom4jFieldType) {
				getValueByElement(root, fieldType, fieldName, fieldValue);
            }
            
            if (Dom4JFieldType.NONE == dom4jFieldType) {
            	root.setText(fieldValue.toString());
            }
		}
		return root;
	}

	/**
     * 从子标签获取复杂属性值
     * 
     * @param root  目标对象
     * @param field      字段对象
     * @param fieldValue 源对象属性
     */
	private void getValueByTag(Element root, Field field, String fieldName, Object fieldValue) {
		boolean isFired = trySimpleValueByTag(root, field.getType(), fieldName, fieldValue);
		isFired = isFired || tryCollectionOrArray(root, field, fieldValue);
		isFired = isFired || tryCustomType(root, field.getType(), fieldValue);
        log.debug("处理结果: " + isFired);
	}
	
	/**
     * 使用子标签获取集合值
     * 
     * @param root
     * @param field
     * @param fieldValue
     * @return
     */
	private boolean tryCollectionOrArray(Element root, Field field, Object fieldValue) {
		Class<?> fieldType = field.getType();

        // 列表&数组
        FieldUtils.CollectionType collectionType = FieldUtils.isCollection(fieldType);
        if (collectionType == null || (FieldUtils.CollectionType.LIST != collectionType
                && FieldUtils.CollectionType.SET != collectionType))
            return false;

        // List & Set
        // 获取泛型类型
        // 如果没有泛型不设置当前值
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            // 泛型类型必须被 Dom4JTag 注解, 否则不予解析
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> childType = (Class<?>) pt.getActualTypeArguments()[0];
            if (!childType.isAnnotationPresent(Dom4JTag.class)) {
                log.warn("请检查泛型类型[" + childType.getName() + "]是否添加 @Dom4JTag 注解");
                return false;
            }

            List<Node> children = Lists.newArrayList();
            for (Object obj : CollectionUtils.cast((List<?>) fieldValue)) {
                if (obj == null)
                    continue;
                Element childTag = fromBean1(obj);
				children.add(childTag);
            }
            root.setContent(children);
            return true;
        }
        return false;
	}

	/**
     * 使用子标签获取简单值
     *
     * @param root      数据对象
     * @param fieldType 字段类型
     * @param fieldName 字段名称
     * @param target    目标标签
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
	private boolean trySimpleValueByTag(Element root, Class<?> fieldType, String fieldName, Object fieldValue) {
        // 只处理简单对象
        if (!FieldUtils.isSimpleType(fieldType))
            return false;

        getValueByElement(root, fieldType, fieldName, fieldValue);
        return true;
    }
	
	/**
     * 使用子标签获取自定义类型值
     * 
     * @param xmlObject
     * @param field
     * @param fieldValue
     * @return
     */
    private boolean tryCustomType(Element root, Class<?> fieldType, Object fieldValue) {
    	// 自定义类型需要加 Dom4JTag 注解
        if (!fieldType.isAnnotationPresent(Dom4JTag.class))
            return false;
        if (fieldValue == null)
            return false;
        
    	Dom4JTag dom4jTag = fieldType.getAnnotation(Dom4JTag.class);
		String childTagName = getTargetTagName(dom4jTag, fieldType.getSimpleName());
		Element children = DocumentHelper.createElement(childTagName);

		Element childTag = fromBean1(fieldValue);
		children.add(childTag);
//		root.add(children);
		
        return true;
    }

    /**
     * 从文本内容获取值
     * 
     * @param root
     * @param fieldType
     * @param fieldName
     * @param fieldValue
     */
	private static void getValueByElement(Element root, Class<?> fieldType, String fieldName, Object fieldValue) {
		Element children = DocumentHelper.createElement(fieldName);
		String xmlValue = ConvertUtils.convert(fieldValue, String.class).toString();
		children.setText(xmlValue);
		root.add(children);
	}

	/**
     * 从子标签获取复杂属性值
     * 
     * @param root  目标对象
     * @param field      字段对象
     * @param fieldValue 源对象属性
     */
	private static void getValueByAttribute(Element root, Class<?> fieldType, String fieldName, Object fieldValue) {
		String xmlValue = ConvertUtils.convert(fieldValue, String.class).toString();
		root.addAttribute(fieldName, xmlValue);
	}
	
	/**
	 * 获取属性映射的目标标签或属性名称
	 *
	 * @param dom4jField  映射注解
	 * @param defaultName 默认名称
	 * @return 映射名称
	 */
	private static String getTargetTagName(Dom4JField dom4jField, String defaultName) {
		return StringUtils.defaultIfBlank(dom4jField.name(), defaultName);
	}
    private static String getTargetTagName(Dom4JTag dom4jTag, String defaultName) {
        return StringUtils.defaultIfBlank(dom4jTag.value(), defaultName);
    }
}
