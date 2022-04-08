package com.eplugger.xstream;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import lombok.Data;

/**
 * <pre>
 * Xstream介绍 注解使用
 * 
 * @author minghui
 *
 */
public class Test2 {
	@Test
	public void testBeanToXml() throws Exception {
		Person bean = new Person(1, "张三", "男", 19, true, "李四", "王五", "赵六");
		XStream xstream = new XStream(new StaxDriver());// 不需要XPP3库开始使用Java6
		xstream.allowTypes(new Class[] { Person.class });
		// 应用指定类的注解
		xstream.processAnnotations(Person.class);
		// 自动检测注解
//		xstream.autodetectAnnotations(true);
		// XML序列化
		String xml = xstream.toXML(bean);
		System.out.println(xml);
		// XML反序列化
		bean = (Person) xstream.fromXML(xml);
		System.out.println(bean);
	}

	@Data
	@XStreamAlias("人") // 别名注解
	private class Person {
		@XStreamAsAttribute // 把字段节点设置成属性
		@XStreamAlias("编号")
		private int id;
		@XStreamAlias("姓名")
		private String name;
		@XStreamOmitField // 隐藏字段
		@XStreamAlias("性别")
		private String sex;
		@XStreamAlias("年龄")
		private int age;
		@XStreamAlias("朋友")
		@XStreamImplicit // 只隐藏集合根节点
//		@XStreamImplicit(itemFieldName="朋友")//设置重复的节点名，可能会导致无法反序列化
		private List<String> friends;
		@XStreamConverter(value = BooleanConverter.class, booleans = { false }, strings = { "在职", "离职" })
		private boolean state;

		public Person(int id, String name, String sax, int age, boolean state, String... friends) {
			this.id = id;
			this.name = name;
			this.sex = sax;
			this.age = age;
			this.state = state;
			this.friends = Arrays.asList(friends);
		}

		@Override
		public String toString() {
			return "Person [id=" + id + ", name=" + name + ", sex=" + sex + ", age=" + age + ", friends=" + friends
					+ ", state=" + state + "]";
		}
	}
}
