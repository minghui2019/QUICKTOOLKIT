package com.eplugger.trans;

import java.util.HashMap;

public class CharMatcherFactory {
	private static class CharMatcherFactorySingleton {
		private static CharMatcherFactory instance = new CharMatcherFactory();
	}
	
	private static final HashMap<Class<?>, CharMatcherHandler> STORE = new HashMap<>();
	private static final CharMatcherHandler DEFAULT = new DefaultCharMatcherHandler();

	public static CharMatcherFactory getFactory() {
		return CharMatcherFactorySingleton.instance;
	}
	
	public static void getCharMatcher(String resurce) {
		CharMatcherFactory factory = getFactory();
	}
	
	public static void register() {

	}

}
