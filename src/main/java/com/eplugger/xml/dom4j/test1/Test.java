package com.eplugger.xml.dom4j.test1;

import org.dom4j.Document;
import org.dom4j.Element;

public class Test {
	private static ReqSchool makeReq() {
		ReqSchool rspSchool = new ReqSchool();
		// 学校编号
		rspSchool.setNumber("1001");
		// 学校名称
		rspSchool.setName("实验小学");
		// 学校省份
		rspSchool.setProvince("北京市");
		// 学校地区
		rspSchool.setAddress("西城区");
		// 学生班级
		rspSchool.setStuclass("高一（2）班");
		// 学生姓名
		rspSchool.setStuname("张三");
		// 学生成绩
		rspSchool.setStuscore("92");
		return rspSchool;
	}

	public static void main(String[] args) {
		// 定义请求模版
		String requestXml = "<?xml version=\"1.0\" encoding = \"GBK\"?>\n" + "<SCHOOL>\n" + "<Head>\n"
				+ "<number>${number}</number>\n" + "<name>${name}</name>\n" + "<province>${province}</province>\n"
				+ "<address>${address}</address>\n" + "</Head>\n" + "<Body>\n" + "<stuclass>${stuclass}</stuclass>\n"
				+ "<stuname>${stuname}</stuname>\n" + "<stuscore>${stuscore}</stuscore>\n" + "</Body>\n" + "</SCHOOL>";
		// 这里我直接使用构造方法（实际开发应以线程安全的单例模式）
		XmlUtil xmlUtil = new XmlUtil();
		// 获取document对象
		Document document = xmlUtil.getDocument(requestXml);
		// 获取根元素
		Element root = document.getRootElement();
		// 请求实体bean
		ReqSchool reqSchool = makeReq();
		// 解析xml，1：表示java2xml
		xmlUtil.parseXml(root, "1", reqSchool);
		// 输出请求报文
		System.out.println(root.asXML());
	}
}
