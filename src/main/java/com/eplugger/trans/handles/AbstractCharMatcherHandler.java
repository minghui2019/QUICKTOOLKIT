package com.eplugger.trans.handles;

import java.util.regex.Matcher;

import com.eplugger.trans.CharMatcherHandler;

import lombok.extern.slf4j.Slf4j;


/**
 * 把匹配的字符替换成空白，前后字符串不用调换位置
 * @author minghui
 */
@Slf4j
public abstract class AbstractCharMatcherHandler implements CharMatcherHandler {
	/**
	 * 把匹配的字符替换成空白，前后字符串不用调换位置
	 */
	@Override
	public String matcherChar(String resource) {
		log.debug("源词组为: " + resource);
		Matcher matcher = getMatches(resource);
		if (matcher.matches()) {
			resource = matcher.group(1) + " " + matcher.group(2);
			log.debug("处理后的词组为：" + matcher.group(2) + " " + matcher.group(1));
		}
		return resource;
	}
	
	/**
	 * 子类覆盖，同时声明正则表达式
	 * @param resource
	 * @return
	 */
	protected abstract Matcher getMatches(String resource);

	@Override
	public boolean caseAnastomotic(String resource) {
		return resource.toLowerCase().indexOf(getTarget()) != -1;
	}
}
