package com.eplugger.onekey.addField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import com.eplugger.onekey.entity.ModuleTable;
import com.eplugger.onekey.entity.ModuleTables;
import com.eplugger.xml.dom4j.util.XmlFileUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.io.xml.XppReader;

import io.github.xstream.mxparser.MXParser;

public class WriteModuleTableXmlTest {
	
	@Test
	public void testWriteXml() throws Exception {
//		WriteModuleTableXmlTest.writeXmlByDom4J();
		ModuleTables moduleTables2 = new ModuleTables();
		List<ModuleTable> moduleTables = moduleTables2.getModuleTableList();
		ModuleTable moduleTable = new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员");
		moduleTables.add(moduleTable);
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		moduleTables.add(new ModuleTable("xJProjectMember", "BIZ_XJ_PROJECT_MEMBER", true, "校级项目成员"));
		list2Xml(moduleTables2);
	}
	
	@Test
	public void testReadXml() throws Exception {
		ModuleTables xml2List = xml2List();
		System.out.println(xml2List);
	}
	
	private void list2Xml(ModuleTables moduleTables) {
//		xstream.processAnnotations(ModuleTables.class);//应用Person类的注解
//		xstream.processAnnotations(ModuleTable.class);//应用Person类的注解
		xstream.autodetectAnnotations(true);//自动检测注解
//		xstream.alias(moduleTables.getClass().getSimpleName(), moduleTables.getClass());
//		xstream.addImplicitCollection(ModuleTables.class, "moduleTables");//省略集合根节点
//		xstream.alias(ModuleTable.class.getSimpleName(), ModuleTable.class);
//		xstream.useAttributeFor(ModuleTable.class, "ignore");
//		OutputStream out = null;
//		try {
//			out = new FileOutputStream(new File("d:/moduleTables.xml"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		xstream.toXML(moduleTables, out);
		String xml = xstream.toXML(moduleTables);
		System.out.println(xml);
		
		XStream xStream1 = new XStream(new StaxDriver());
//		xStream1.processAnnotations(ModuleTables.class);//应用Person类的注解
//		xStream1.processAnnotations(ModuleTable.class);//应用Person类的注解
		xStream1.allowTypes(new Class[]{ModuleTables.class, ModuleTable.class});
//		xStream1.alias(ModuleTables.class.getSimpleName(), ModuleTables.class);
//		xStream1.alias(ModuleTable.class.getSimpleName(), ModuleTable.class);
//		xStream1.useAttributeFor(ModuleTable.class, "ignore");
//		xStream1.addImplicitCollection(ModuleTables.class, "moduleTables");//省略集合根节点
		Object fromXML = xStream1.fromXML(xml);
		System.out.println(fromXML);
		System.out.println(123);
	}
	
	private ModuleTables xml2List() throws FileNotFoundException {
//		HierarchicalStreamDriver driver =  new AbstractXppDriver() {
//			@Override
//			protected XmlPullParser createParser() throws XmlPullParserException {
//				return null;
//			}
//		};
		XStream xstream1 = new XStream();
		xstream1.alias(ModuleTables.class.getSimpleName(), ModuleTables.class);
		xstream1.addImplicitCollection(ModuleTables.class, "moduleTables");//省略集合根节点
		xstream1.alias(ModuleTable.class.getSimpleName(), ModuleTable.class);
		xstream1.useAttributeFor(ModuleTable.class, "ignore");
		FileInputStream fis = new FileInputStream(new File("d:/moduleTables.xml"));
		Object fromXML = xstream1.fromXML(fis);
		return (ModuleTables) fromXML;
	}
	
	private static Document readXmlByFile() {
		Document document = null;
		try {
			document = XmlFileUtils.readDocument("src/main/resource/field/ModuleTable.xml");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	private static void writeXmlByDom4J() {
        try {
//            // 创建文档并设置文档的根元素节点   
//            Element root = DocumentHelper.createElement("peoples");  
//            
//            Document document = DocumentHelper.createDocument(root);  
//            //根节点  
//            root.addAttribute("name","peoplesvalue");  
//            //子节点  
//            Element element1 = root.addElement("people");  
//            element1.addAttribute( "id", "1" );
//            element1.addElement("name", "小王"); 
//            element1.addElement("age","22");
//
//            Element element2 = root.addElement("people");  
//            element2.addAttribute( "id", "2" );
//            element2.addElement("name", "老王"); 
//            element2.addElement("age","23");
        	Document document = readXmlByFile();
            //添加  
            XMLWriter xmlwriter2 = new XMLWriter();  
            xmlwriter2.write(document);  
            //创建文件  
            OutputFormat format = new OutputFormat();
            format.setIndentSize(4);
            format.setNewlines(true);
            format.setTrimText(true);
            format.setPadText(true);
            //设定编码
            format.setEncoding("UTF-8");
            XMLWriter xmlwriter = new XMLWriter(new FileOutputStream("d:/dom4jpeople.xml"), format);
            xmlwriter.write(document);
            xmlwriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
	
	/**
	 * 扩展xstream，使其支持CDATA块
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
//				boolean cdata = false;

//				@Override
//				public void startNode(String name, Class clazz) {
//					super.startNode(name.substring(0, 1).toUpperCase() + name.substring(1), clazz);
//				}
				
//				@Override
//				protected void writeText(QuickWriter writer, String text) {
//					if (cdata) {
//						writer.write("<![CDATA[");
//						writer.write(text);
//						writer.write("]]>");
//					} else {
//						writer.write(text);
//					}
//				}
			};
		}
		
		@Override
		public HierarchicalStreamReader createReader(Reader in) {
			return new XppReader(in, new MXParser()) {
				@Override
				protected String pullElementName() {
					String name = super.pullElementName();
//					System.out.println(name);
					return name.substring(0, 1).toLowerCase() + name.substring(1);
				};
			};
		};
	});
	
	public static void object2Xml(ModuleTable moduleTable) {
		xstream.alias(moduleTable.getClass().getSimpleName(), moduleTable.getClass());
		xstream.useAttributeFor(moduleTable.getClass(), "ignore");
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File("d:/moduleTable.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		xstream.toXML(moduleTable, out);
	}
}
