package com.eplugger.onekey.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>模块信息</p>
 * @author Admin
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Module {
	@Dom4JField(type = Dom4JFieldType.TAG, name = "MainModule")
	private ModuleInfo mainModule;
	@Dom4JField(type = Dom4JFieldType.TAG, name = "AuthorModule")
	private ModuleInfo authorModule;
	@Dom4JField(type = Dom4JFieldType.ATTRIBUTE, comment = "xml配置文件是否配置忽略")
	private boolean ignore = false;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\nModule [");
		if (mainModule != null) {
			sb.append("mainModule=").append(mainModule).append(", \n");
		}
		if (authorModule != null) {
			sb.append("authorModule=").append(authorModule).append(", \n");
		}
		if (!ignore) {
			sb.append("ignore=").append(ignore).append(", \n");
		}
		if (sb.lastIndexOf(",") != -1) {
			return sb.substring(0, sb.length() - 3) + "]";
		}
		return sb.toString() + "]";
	}
}
