package com.eplugger.trans.handles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForCharMatcherHandler extends AbstractCharMatcherHandler {
	private static Pattern patternFor = Pattern.compile("([\\w ]+) [f|F]or ([\\w ]+)");

	@Override
	public String getTarget() {
		return "for";
	}

	@Override
	protected Matcher getMatches(String resource) {
		return patternFor.matcher(resource);
	}
}
