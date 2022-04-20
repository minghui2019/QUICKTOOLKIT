package com.eplugger.trans.handles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndCharMatcherHandler extends AbstractCharMatcherHandler {
	private static Pattern patternAnd = Pattern.compile("([\\w ]+) [a|A]nd ([\\w ]+)");
	
	@Override
	public String getTarget() {
		return "and";
	}

	@Override
	protected Matcher getMatches(String resource) {
		return patternAnd.matcher(resource);
	}
}
