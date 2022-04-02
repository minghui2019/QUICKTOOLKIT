package com.eplugger.onekey.addField;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.xml.sax.SAXException;

public class WriteModuleTableXmlTest {
	
	@Test
	public void testWriteXml() throws Exception {
		WriteModuleTableXmlTest.writeXmlByDom4J();
	}
	
	private static void writeXmlByDom4J() {
        try {
            // 创建文档并设置文档的根元素节点   
            Element root = DocumentHelper.createElement("peoples");  
            
            Document document = DocumentHelper.createDocument(root);  
            //根节点  
            root.addAttribute("name","peoplesvalue");  
            //子节点  
            Element element1 = root.addElement("people");  
            element1.addAttribute( "id", "1" );
            element1.addElement("name", "小王"); 
            element1.addElement("age","22");

            Element element2 = root.addElement("people");  
            element2.addAttribute( "id", "2" );
            element2.addElement("name", "老王"); 
            element2.addElement("age","23");
            //添加  
            XMLWriter xmlwriter2 = new XMLWriter();  
            xmlwriter2.startDTD("ModuleTables", "", "../dtd/ModuleTable.dtd");
            xmlwriter2.write(document);  
            //创建文件  
            OutputFormat format = OutputFormat.createPrettyPrint();  
            //设定编码
            format.setEncoding("UTF-8");
            XMLWriter xmlwriter = new XMLWriter(new FileOutputStream("d:/dom4jpeople.xml"), format);
            try {
            	xmlwriter.startDTD("ModuleTables", "", "../dtd/ModuleTable.dtd");
            	xmlwriter.write(document);
            } catch (SAXException e) {
            	e.printStackTrace();
            }
            xmlwriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
    }
}
