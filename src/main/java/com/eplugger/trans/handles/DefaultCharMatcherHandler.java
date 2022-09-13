package com.eplugger.trans.handles;

import com.eplugger.trans.CharMatcherHandler;

/**
 * 默认的处理器，不作任何处理
 * @author minghui
 *
 */
public class DefaultCharMatcherHandler implements CharMatcherHandler {
	@Override
	public String matcherChar(String resource) {
		return resource;
	}

	@Override
	public String getTarget() {
		return "default";
	}

	@Override
	public boolean caseAnastomotic(String resource) {
		return false;
	}
}
