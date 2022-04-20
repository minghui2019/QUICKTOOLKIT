package com.eplugger.trans;

/**
 * <pre>
 * 特别的字符串处理器
 * 比如：
 * *** for ***
 * *** and ***
 * *** of ***
 * ...
 * </pre>
 * @author Admin
 */
public interface CharMatcherHandler {

	/**
	 * 利用正则对字符串进行处理
	 * @param resource 源字符串
	 * @return 处理后的字符串
	 */
	String matcherChar(String resource);

	/**
	 * <pre>
	 * 用于注册的key
	 * 比如: of, for, ...
	 * </pre>
	 * @return
	 */
	String getTarget();

	/**
	 * 字符串是否包含 {@link com.eplugger.trans.CharMatcherHandler#getTarget() getTarget()} 的单词
	 * @param resource
	 * @return
	 */
	boolean caseAnastomotic(String resource);
}
