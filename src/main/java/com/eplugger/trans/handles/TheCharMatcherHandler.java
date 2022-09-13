package com.eplugger.trans.handles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eplugger.trans.CharMatcherHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TheCharMatcherHandler implements CharMatcherHandler {
	private static Pattern patternThe = Pattern.compile("([\\w ]+) [t|T]he ([\\w ]+)");
	
	@Override
	public String getTarget() {
		return "the";
	}

	@Override
	public boolean caseAnastomotic(String resource) {
		return resource.toLowerCase().indexOf(getTarget()) != -1;
	}
	
	@Override
	public String matcherChar(String resource) {
		log.debug("源词组为: " + resource);
		if (resource.startsWith("the")) {
			resource = resource.replace("the ", "");
		} else if (resource.startsWith("The")) {
			resource = resource.replace("The ", "");
		} else {
			Matcher matcher = patternThe.matcher(resource);
			if (matcher.matches()) {
				resource = matcher.group(1) + " " + matcher.group(2);
			}
		}
		log.debug("处理后的词组为：" + resource);
		return resource;
	}
}
