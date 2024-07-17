package com.eplugger.trans.handles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eplugger.trans.CharMatcherHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * of处理器，交换前后匹配字符串位置
 * @author minghui
 */
@Slf4j
public class OfCharMatcherHandler implements CharMatcherHandler {
	private static Pattern patternOf = Pattern.compile("([\\w ]+) [o|O]f ([\\w ]+)");

	@Override
	public String matcherChar(String resource) {
		Matcher matcher = patternOf.matcher(resource);
		if (matcher.matches()) {
			log.debug("源词组为: " + resource);
			resource = matcher.group(2) + " " + matcher.group(1);
			log.debug("处理后的词组为：" + resource);
		}
		return resource;
	}
	
	@Override
	public String getTarget() {
		return "of";
	}

	@Override
	public boolean caseAnastomotic(String resource) {
		return resource.toLowerCase().indexOf(getTarget()) != -1;
	}
}
