package com.eplugger.main;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader; 
 
/** 
* Created by IntelliJ IDEA.<br> 
* <b>User</b>: leizhimin<br> 
* <b>Date</b>: 2008-4-14 14:02:12<br> 
* <b>Note</b>: Java递归遍历XML所有元素 
*/ 
public class XmlTest { 
    //存储xml元素信息的容器 
    private static List<Leaf> elemList = new ArrayList<Leaf>(); 
    private static Map<String, Object> eleMap = new HashMap<String, Object>();
    private static Stack<String> stack = new Stack<String>();
 
    public static void main(String args[]) throws DocumentException { 
        XmlTest test = new XmlTest(); 
        Element root = test.getRootElement(); 
        test.getElementList(root); 
        String x = test.getListString(elemList); 
 
//        System.out.println("-----------原xml内容------------"); 
//        System.out.println(srcXml); 
        System.out.println("-----------解析结果------------"); 
//        System.out.println(x); 
        
        test.getElementMap(root);
        System.out.println(eleMap);
 
    } 
 
    /** 
     * 获取根元素 
     * 
     * @return 
     * @throws DocumentException 
     */ 
    public Element getRootElement() throws DocumentException {
    	//1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        Document read = reader.read(new File("src/resource/module/Module.xml"));
        return read.getRootElement();
    } 
 
    /** 
     * 递归遍历方法 
     * 
     * @param element 
     */ 
	public void getElementList(Element element) {
		List<?> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			String name = element.getName();
			String xpath = element.getPath();
			String value = element.getTextTrim();
			elemList.add(new Leaf(xpath, value, name));
		} else {
			// 有子元素
			String name = element.getName();
			System.out.println(name);
			for (Iterator<?> it = elements.iterator(); it.hasNext();) {
				Element elem = (Element) it.next();
				// 递归遍历
				getElementList(elem);
			}
		}
	}
    
	public Object getElementMap(Element element) {
		List<?> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			String name = element.getName();
			String xpath = element.getPath();
			String value = element.getTextTrim();
			return new Leaf(xpath, value, name);
		} else {
			// 有子元素
			stack.push(element.getName());
			String name = element.getName();
			List<Object> list = new ArrayList<Object>();
			System.out.println(name);
			for (Iterator<?> it = elements.iterator(); it.hasNext();) {
				Element elem = (Element) it.next();
				// 递归遍历
				list.add(getElementMap(elem));
			}
			eleMap.put(stack.pop(), list);
		}
		return null;
	}
    
 
    public String getListString(List<Leaf> elemList) { 
        StringBuffer sb = new StringBuffer(); 
        for (Iterator<Leaf> it = elemList.iterator(); it.hasNext();) { 
            Leaf leaf = it.next(); 
            sb.append(leaf.getXpath()).append(" | ").append(leaf.getName()).append(" = ").append(leaf.getValue()).append("\n"); 
        } 
        return sb.toString(); 
    } 
} 
 
/** 
* xml节点数据结构 
*/ 
class Leaf { 
    private String xpath;         // 
    private String value; 
    private String name; 
    
 
    public Leaf(String xpath, String value, String name) {
		super();
		this.xpath = xpath;
		this.value = value;
		this.name = name;
	}

	public Leaf(String xpath, String value) { 
        this.xpath = xpath; 
        this.value = value; 
    } 
 
    public String getXpath() { 
        return xpath; 
    } 
 
    public void setXpath(String xpath) { 
        this.xpath = xpath; 
    } 
 
    public String getValue() { 
        return value; 
    } 
 
    public void setValue(String value) { 
        this.value = value; 
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Leaf [xpath=" + xpath + ", value=" + value + ", name=" + name + "]\n";
	} 
}