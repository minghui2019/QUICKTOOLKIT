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
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.eplugger.common.lang.StringUtils;
import com.eplugger.commons.collections.CollectionUtils;
import com.eplugger.commons.lang3.reflect.FieldUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.eplugger.xml.dom4j.parse.FieldValueParserFactory;
import com.eplugger.xml.dom4j.util.XmlFileUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dom4JParser {
	/**
	 * XML 文件路径
	 */
	private final String path;

	/**
	 * 文件编码, 默认使用 UTF-8
	 */
	private final String encoding;
	
	/**
	 * 
	 */
	private DocType docType;

	/**
	 * 构建XML解析器
	 *
	 * @param path 文件路径
	 */
	public Dom4JParser(String path) {
		this(path, "UTF-8");
	}
	
	public Dom4JParser(String path, DocType docType) {
		this(path, "UTF-8", docType);
	}

	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param encoding 文件编码
	 */
	public Dom4JParser(String path, Charset encoding) {
		this(path, encoding.name());
	}

	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param encoding 文件编码
	 */
	public Dom4JParser(String path, String encoding) {
		this.path = path;
		this.encoding = encoding;
	}
	
	public Dom4JParser(String path, String encoding, DocType docType) {
		this(path, encoding);
		this.docType = docType;
	}

	/**
	 * 获取XML文件根节点
	 *
	 * @return Document 根节点
	 * @throws DocumentException 
	 */
	private Document getDocument() {
		try {
			return XmlFileUtils.readDocument(path, encoding);
		} catch (DocumentException e) {
			log.error("xml文件读取失败, 请检查路径[path=" + path + "]" + e.getMessage());
		}
		return null;
	}

	/**
	 * 映射为实体类
	 * @param cls
	 * @return
	 */
	public <T> T toBean(Class<T> cls) {
		Dom4JTag xmlTag = cls.getAnnotation(Dom4JTag.class);
        if (xmlTag == null)
        	throw new RuntimeException("类型[" + cls.getSimpleName() + "]未使用 @Dom4JTag 注解");
        
        Document document = getDocument();
        if (document == null)
        	return null;
        Element root = document.getRootElement();
        
        String expectName = StringUtils.trimToEmpty(xmlTag.value());
        String tagName = root.getName();
        if (StringUtils.isNotBlank(expectName) && !expectName.equals(tagName))
            throw new RuntimeException("期望标签名[" + expectName + "]与实际标签名[" + tagName + "]不一致");
        
        return toBean(cls, root);
	}

	/**
	 * 映射为实体类; 要注册自定义解析器需要在 {@link FieldValueParserFactory} 中注册 并且必须在调用当前方法之前.
	 *
	 * @param cls 实体类字节码
	 * @param     <T> 实体类泛型
	 * @return 实体类对象
	 * @throws Exception
	 * @see FieldValueParserFactory 字段实例解析器工厂
	 */
	public <T> T toBean(Class<T> cls, Element element) {
		T bean;
		try {
			bean = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("类型[" + cls.getSimpleName() + "]创建实例失败，请检查是否缺少无参数的构造函数");
		}

		// 获取字段列表
		List<Field> fields = FieldUtils.getAllFieldsList(cls);
		for (Field field : fields)
			if (!FieldUtils.isStaticOrFinal(field))
				setValue(bean, field, element);

		return bean;
	}
	
	/**
	 * 指定控件解析为列表
	 *
	 * @param childTagName 子标签名
	 * @param cls          实体类字节码
	 * @param root         当前解析的根元素
	 * @return
	 * @see #toBean(Class, Element)
	 */
	public <T> List<T> toBeans(String childTagName, Class<T> cls, Element root) {
		List<T> beans = Lists.newArrayList();

		List<Element> elements = root.elements();
		for (Element element : elements) {
			T bean = toBean(cls, element);
			beans.add(bean);
		}
		return beans;
	}

	/**
	 * 设置值
	 *
	 * @param bean    数据对象
	 * @param field   字段
	 * @param element
	 */
	private void setValue(Object bean, Field field, Element element) {
		// 不处理没有添加字段注解的属性
		Dom4JField dom4jField = field.getAnnotation(Dom4JField.class);
		if (null == dom4jField)
			return;

		String name = getTargetTagName(dom4jField, field.getName());
		Dom4JFieldType type = dom4jField.type();
		try {
			switch (type) {
			// 属性直接映射
			case ATTRIBUTE:
				String value = element.attributeValue(name);
				setFieldValue(bean, field, value);
				break;
			// 子标签
			case TAG:
				setValueByTag(bean, field, dom4jField, element);
				break;
			// 文本值
			case ELEMENT:
				String text = element.elementText(name);
				setFieldValue(bean, field, text);
				break;
			case NONE:
				setFieldValue(bean, field, element.getTextTrim());
				break;
			}
		} catch (Exception e) {
			throw new RuntimeException(e + field.getName());
		}
	}

	/**
	 * 从子标签填充复杂属性值
	 *
	 * @param bean     目标对象
	 * @param field    字段对象
	 * @param xmlField XML字段映射注解
	 * @param element
	 */
	private void setValueByTag(Object bean, Field field, Dom4JField xmlField, Element element) throws Exception {
		boolean isFired = trySimpleValueByTag(bean, field, element);
		isFired = isFired || tryCollectionOrArray(bean, field, element);
		isFired = isFired || tryCustomType(bean, field, element);
		log.debug("path&hierarchy处理结果: " + isFired);
	}

	/**
	 * 尝试自定义类型
	 *
	 * @param bean    数据对象
	 * @param field   字段对象
	 * @param element
	 * @return 成功处理返回true, 否则返回false(需要其他方式处理)
	 */
	private boolean tryCustomType(Object bean, Field field, Element element) throws IllegalAccessException {
		Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
		Class<?> fieldType = field.getType();
		// 自定义类型
		if (!fieldType.isAnnotationPresent(Dom4JTag.class))
			return false;

		// 复合属性只能解析唯一的子标签
		// 如果标签出现多个证明实体类属性类型定义错误
		String childTagName = getTargetTagName(xmlField, fieldType.getSimpleName());
		List<Element> children = element.elements(childTagName);
		if (children.size() > 1)
            throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");
		//xml没找到则跳过
		if (children.size() == 0)
			return false;
        
		FieldUtils.setFieldValue(bean, field, toBean(fieldType, children.get(0)));
		return true;
	}

	/**
	 * 使用子标签设置简单值
	 *
	 * @param bean    数据对象
	 * @param field   字段对象
	 * @param element 目标标签
	 * @return 成功处理返回true, 否则返回false(需要其他方式处理)
	 */
	private boolean trySimpleValueByTag(Object bean, Field field, Element element) throws Exception {
		Class<?> fieldType = field.getType();
		Dom4JField xmlField = field.getAnnotation(Dom4JField.class);

		// 只处理简单对象
		if (!FieldUtils.isSimpleType(fieldType))
			return false;

		// 子节点个数检查
		String childTagName = getTargetTagName(xmlField, fieldType.getSimpleName());
		List<Element> children = element.elements(childTagName);

		if (children.size() > 1)
            throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");
		//xml没找到则跳过
		if (children.size() == 0)
			return false;

		setFieldValue(bean, field, children.get(0).getTextTrim());
		return true;
	}

	/**
     * 尝试设置列表或者数组
     *
     * @param bean  数据对象
     * @param field 字段对象
     * @param element
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
	private boolean tryCollectionOrArray(Object bean, Field field, Element element) throws IllegalAccessException {
		Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
		Class<?> fieldType = field.getType();

		// 列表&数组
		FieldUtils.CollectionType collectionType = FieldUtils.isCollection(fieldType);

		// List & Set
		if (FieldUtils.CollectionType.LIST == collectionType || FieldUtils.CollectionType.SET == collectionType) {
			// 获取泛型类型
			// 如果没有泛型不设置当前值
			Type genericType = field.getGenericType();
			if (!(genericType instanceof ParameterizedType)) {
				return false;
			}
			// 泛型类型必须被 Dom4JTag 注解, 否则不予解析
			ParameterizedType pt = (ParameterizedType) genericType;
			Class<?> type = (Class<?>) pt.getActualTypeArguments()[0];
			if (!type.isAnnotationPresent(Dom4JTag.class)) {
				log.warn("请检查泛型类型[" + type.getName() + "]是否添加 @Dom4JTag 注解");
				return true;
			}

			String childTagName = getTargetTagName(xmlField, type.getSimpleName());
			List<?> val = toBeans(childTagName, type, element);

			// 如果目标集合是Set集合, 从List集合转
			if (FieldUtils.CollectionType.SET == collectionType)
				FieldUtils.setFieldValue(bean, field, Sets.newLinkedHashSet(val));
			else
				FieldUtils.setFieldValue(bean, field, val);
			return true;
		}
		
		// Array
        if (FieldUtils.CollectionType.ARRAY == collectionType) {
            Class<?> type = fieldType.getComponentType();
            String childTagName = getTargetTagName(xmlField, type.getSimpleName());
            List<?> val = toBeans(childTagName, type, element);
            FieldUtils.setFieldValue(bean, field, val.toArray());
            return true;
        }

		return false;
	}
	
	/****************************** fromBean ******************************/
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
		if (this.docType != null)
			document.setDocType(DocumentFactory.getInstance().createDocType(this.docType.elementName, this.docType.publicID, this.docType.systemID));
		
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
            	String xmlValue = ConvertUtils.convert(fieldValue, String.class).toString();
        		root.addAttribute(fieldName, xmlValue);
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
	
	/****************************** 共同方法 ******************************/
	/**
	 *  TODO 
	 *  待换
	 * @see ConvertUtils#convert(String, Class)
	 * @param bean
	 * @param field
	 * @param value
	 * @throws IllegalAccessException
	 */
	private void setFieldValue(Object bean, Field field, String value) throws IllegalAccessException {
		if (Strings.isNullOrEmpty(value))
    		return;

		Class<?> type = field.getType();
		Object obj = ConvertUtils.convert(value, type);
		FieldUtils.setFieldValue(bean, field, obj);
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