package com.eplugger.xstream;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import lombok.Data;

/**
 * <pre>Xstream介绍
 * 常规使用
 * @author minghui
 *
 */
public class Test1 {
	@Test
	public void testBeanToXml() throws Exception {
		Person bean = new Person(1, "张三", "男", 19, "李四", "王五", "赵六");
		XStream xstream = new XStream(new StaxDriver());// 不需要XPP3库开始使用Java6
		xstream.allowTypes(new Class[] { Person.class });
		// 1, 2互斥
		// 1.为类名节点重命名
		xstream.alias("人", Person.class);
		// 2.为包名称重命名
//		xstream.aliasPackage("xml", "com.eplugger.xstream");
		// 3.为类的字段节点重命名
		xstream.aliasField("编号", Person.class, "id");
		xstream.aliasField("姓名", Person.class, "name");
		xstream.aliasField("性别", Person.class, "sax");
		xstream.aliasField("年龄", Person.class, "age");
		// 4.省略集合根节点
		xstream.addImplicitCollection(Person.class, "friends");
		// 5.把字段节点设置成属性
		xstream.useAttributeFor(Person.class, "id");
		// 6.把字段节点隐藏
		xstream.omitField(Person.class, "sax");
		// XML序列化
		String xml = xstream.toXML(bean);
		System.out.println(xml);
		// XML反序列化
		bean = (Person) xstream.fromXML(xml);
		System.out.println(bean);
	}

	@Test
	public void testBeanToJson() throws Exception {
		Person bean = new Person(1, "张三", "男", 19, "李四", "王五", "赵六");
		XStream xstream = new XStream(new JettisonMappedXmlDriver());// 设置Json解析器
		xstream.allowTypes(new Class[] { Person.class });
		xstream.setMode(XStream.NO_REFERENCES);// 设置reference模型,不引用
		// 1, 2互斥
		// 1.为类名节点重命名
		xstream.alias("人", Person.class);
		// 2.为包名称重命名
//		xstream.aliasPackage("xml", "com.eplugger.xstream");
		// 3.为类的字段节点重命名
		xstream.aliasField("编号", Person.class, "id");
		xstream.aliasField("姓名", Person.class, "name");
		xstream.aliasField("性别", Person.class, "sax");
		xstream.aliasField("年龄", Person.class, "age");
		// 4.省略集合根节点
		xstream.addImplicitCollection(Person.class, "friends");
		// 5.把字段节点设置成属性
		xstream.useAttributeFor(Person.class, "id");
		// 6.把字段节点隐藏
		xstream.omitField(Person.class, "sax");
		// Json序列化
		String json = xstream.toXML(bean);
		System.out.println(json);
		// Json反序列
		bean = (Person) xstream.fromXML(json);
		System.out.println(bean);
	}
	
	@Data
	private class Person {
		private int id;
		private String name;
		private String sax;
		private int age;
		private List<String> friends;

		public Person(int id, String name, String sax, int age, String... friends) {
			this.id = id;
			this.name = name;
			this.sax = sax;
			this.age = age;
			this.friends = Arrays.asList(friends);
		}

		@Override
		public String toString() {
			return "Person [id=" + id + ", name=" + name + ", sax=" + sax + ", age=" + age + ", friends=" + friends + "]";
		}
	}
}
