package com.eplugger.xml.dom4j.format;

import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.format.formatters.DefaultXMLObjectFormatter;

/**
 * {@link XMLObject}格式化工厂
 *
 * @author Huang.Yong
 */
public class XMLObjectFormatterFactory {

	/**
	 * 创建格式化工具
	 *
	 * @param compact true-紧缩排版的, false-缩进排版的
	 * @return XMLObjectFormatter 格式化工具
	 */
	public static XMLObjectFormatter createFormatter(boolean compact) {
		return new DefaultXMLObjectFormatter(compact);
	}

}
