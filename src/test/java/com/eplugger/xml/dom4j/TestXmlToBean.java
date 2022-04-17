package com.eplugger.xml.dom4j;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.commons.lang3.reflect.FieldUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.eplugger.xml.dom4j.entity.ModuleTables;
import com.eplugger.xml.dom4j.parse.FieldValueParserFactory;
import com.eplugger.xml.dom4j.parser.v14.XMLParserTest4;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXmlToBean {
	/**
	 * XML 文件路径
	 */
	private final String path;

	/**
	 * 文件编码, 默认使用 UTF-8
	 */
	private final String fileEncoding;

	/**
	 * 构建XML解析器
	 *
	 * @param path 文件路径
	 */
	public TestXmlToBean(String path) {
		this(path, StandardCharsets.UTF_8);
	}

	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param fileEncoding 文件编码
	 */
	public TestXmlToBean(String path, String fileEncoding) {
		this.path = path;
		this.fileEncoding = fileEncoding;
	}

	/**
	 * 构建XML解析器
	 *
	 * @param path         文件路径
	 * @param fileEncoding 文件编码
	 */
	public TestXmlToBean(String path, Charset fileEncoding) {
		this(path, fileEncoding.name());
	}

	/**
	 * 获取XML文件根节点
	 *
	 * @return Document 根节点
	 */
	private Document getDocument() throws Exception {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding(fileEncoding);
		File file = getXMLFile();
		return saxReader.read(file);
	}

	/**
	 * 获取XML文件对象
	 *
	 * @return File XML文件对象
	 */
	private File getXMLFile() {
		if (path == null || 0 >= path.length()) {
			throw new RuntimeException("Path invalid[path=" + path + "]");
		}
		return new File(path);
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
			throw new RuntimeException(e);
		}

		// 获取字段列表
		List<Field> fields = FieldUtils.getAllFieldsList(cls);
		for (Field field : fields)
			if (!FieldUtils.isStaticOrFinal(field))
				setValue(bean, field, element);

		return bean;
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
		Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
		if (null == xmlField)
			return;

		String name = getTargetTagName(xmlField, field.getName());
		Dom4JFieldType type = xmlField.type();
		try {
			switch (type) {

			// 属性直接映射
			case ATTRIBUTE:
				String value = element.attributeValue(name);
				setFieldValue(bean, field, value);
				break;

			// 子标签
			case TAG:
				setValueByTag(bean, field, xmlField, element);
				break;

			case ELEMENT:
				String text = element.elementText(name);
				setFieldValue(bean, field, text);
				break;
			}
		} catch (Exception e) {
			throw new RuntimeException(e + field.getName());
		}
	}

	private void setFieldValue(Object bean, Field field, String value) throws IllegalAccessException {
		Class<?> type = field.getType();
		Object obj = ConvertUtils.convert(value, type);
		field.setAccessible(true);
		field.set(bean, obj);
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
		field.setAccessible(true);

		boolean isFired = trySimpleValueByTag(bean, field, element);
		isFired = isFired || tryCollectionOrArray(bean, field, element);
		isFired = isFired || tryCustomType(bean, field, element);
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
		if (1 < children.size())
			throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");

		if (1 == children.size()) {
			Element firstChildTag = children.get(0);
			Object val = toBean(fieldType, firstChildTag);
			field.set(bean, val);
		}

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

		if (1 < children.size())
			throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");

		if (1 == children.size()) {
			Element firstChildTag = children.get(0);
			setFieldValue(bean, field, firstChildTag.getTextTrim());
		}
		return true;
	}

	private boolean tryCollectionOrArray(Object bean, Field field, Element element) throws IllegalAccessException {
		Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
		Class<?> fieldType = field.getType();

		// 列表&数组
		FieldUtils.CollectionType collectionType = FieldUtils.isCollection(fieldType);
		if (null == collectionType)
			return false;

		// List & Set
		if (FieldUtils.CollectionType.LIST == collectionType || FieldUtils.CollectionType.SET == collectionType) {

			// 获取泛型类型
			// 如果没有泛型不设置当前值
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {

				// 泛型类型必须被 XmlTag 注解, 否则不予解析
				ParameterizedType pt = (ParameterizedType) genericType;
				Class<?> type = (Class<?>) pt.getActualTypeArguments()[0];
				if (!type.isAnnotationPresent(Dom4JTag.class)) {
					log.warn("请检查泛型类型[" + type.getName() + "]是否添加 @XmlTag 注解");
					return true;
				}

				String childTagName = getTargetTagName(xmlField, type.getSimpleName());
				List<?> val = toBeans(childTagName, type, element);

				// 如果目标集合是Set集合, 从List集合转
				if (FieldUtils.CollectionType.SET == collectionType)
					field.set(bean, new LinkedHashSet<>(val));
				else
					field.set(bean, val);
			}
			return true;
		}

		return true;
	}

	public <T> List<T> toBeans(String childTagName, Class<T> cls, Element element) {
		List<T> beans = Lists.newArrayList();

		List<Element> elements = element.elements();
		for (Element element1 : elements) {
			T bean = toBean(cls, element1);
			beans.add(bean);
		}
		return beans;
	}

	private static String getTargetTagName(Dom4JField xmlField, String defaultName) {
		return StringUtils.defaultIfBlank(xmlField.name(), defaultName);
	}

	public static void main(String[] args) throws Exception {
//		String xmlPath = XMLParserTest4.class.getResource("/Field.xml").getFile();
        String xmlPath = XMLParserTest4.class.getResource("/ModuleTable.xml").getFile();
		TestXmlToBean testXml = new TestXmlToBean(xmlPath);
		Document document = testXml.getDocument();
		Element rootElement2 = document.getRootElement();
//		Fields bean = testXml.toBean(Fields.class, rootElement2);
        ModuleTables bean = testXml.toBean(ModuleTables.class, rootElement2);
		System.out.println(bean);
	}
}
