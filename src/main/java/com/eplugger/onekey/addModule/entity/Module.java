package com.eplugger.onekey.addModule.entity;

import com.eplugger.annotation.Booleaner;

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
public class Module {
	private ModuleInfo mainModule;
	private ModuleInfo authorModule;
	/** xml配置文件是否配置忽略 */
	@Booleaner
	private Boolean ignore;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Module [");
		if (mainModule != null) {
			sb.append("mainModule=").append(mainModule).append(", \n");
		}
		if (authorModule != null) {
			sb.append("authorModule=").append(authorModule).append(", \n");
		}
		if (ignore != null) {
			sb.append("ignore=").append(ignore).append(", \n");
		}
		return sb.substring(0, sb.length() - 3) + "]";
	}
}
