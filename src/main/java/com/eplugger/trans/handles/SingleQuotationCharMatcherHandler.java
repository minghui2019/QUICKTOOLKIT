package com.eplugger.trans.handles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eplugger.trans.CharMatcherHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleQuotationCharMatcherHandler implements CharMatcherHandler {
	private static Pattern pattern = Pattern.compile("([\\w ]+)'([\\w ]+)");
	
	@Override
	public String getTarget() {
		return "'";
	}

	@Override
	public boolean caseAnastomotic(String resource) {
		return resource.toLowerCase().indexOf(getTarget()) != -1;
	}
	
	@Override
	public String matcherChar(String resource) {
		Matcher matcher = pattern.matcher(resource);
		if (matcher.matches()) {
			log.debug("源词组为: " + resource);
			resource = matcher.group(1) + "’" + matcher.group(2);
			log.debug("处理后的词组为：" + resource);
		}
		return resource;
	}
}
