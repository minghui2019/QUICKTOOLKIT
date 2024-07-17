package com.eplugger.trans;

import java.util.Map;

import com.eplugger.trans.handles.AndCharMatcherHandler;
import com.eplugger.trans.handles.DefaultCharMatcherHandler;
import com.eplugger.trans.handles.ForCharMatcherHandler;
import com.eplugger.trans.handles.OfCharMatcherHandler;
import com.eplugger.trans.handles.SingleQuotationCharMatcherHandler;
import com.eplugger.trans.handles.TheCharMatcherHandler;
import com.google.common.collect.Maps;

/**
 * <pre>
 * 提供词组处理服务
 * 注意：
 * 任何处理器需要实现 {@link com.eplugger.trans.CharMatcherHandler CharMatcherHandler} 接口
 * 或继承 {@link com.eplugger.trans.handles.AbstractCharMatcherHandler AbstractCharMatcherHandler} 抽象类
 * 并于 {@link com.eplugger.trans.CharMatcherHandlerFactory#registerStandard() 工厂私有注册方法 } 注册
 * 或调用 {@link com.eplugger.trans.CharMatcherHandlerFactory#register(CharMatcherHandler) 静态工厂注册方法} 注册
 * 才有效
 * </pre>
 * @author minghui
 */
public class CharMatcherHandlerFactory {
	private static Map<String, CharMatcherHandler> store = Maps.newLinkedHashMap();
	
	private static class CharMatcherFactorySingleton {
		// 利用内部类实现对象唯一性
		private static CharMatcherHandlerFactory instance = new CharMatcherHandlerFactory();
	}
	
	public static CharMatcherHandlerFactory getFactory() {
		return CharMatcherFactorySingleton.instance;
	}
	
	private CharMatcherHandlerFactory() {
		deregister();
	}
	
	private void deregister() {
		registerStandard();
	}

	/**
	 * 内部注册处理器
	 */
	private void registerStandard() {
		register(new TheCharMatcherHandler());
		register(new AndCharMatcherHandler());
		register(new OfCharMatcherHandler());
		register(new ForCharMatcherHandler());
		register(new DefaultCharMatcherHandler());
		register(new SingleQuotationCharMatcherHandler());
	}
	
	/**
	 * 处理器工厂对外服务入口
	 * @param resource
	 * @return
	 */
	public String matcherChar(String resource) {
		for (CharMatcherHandler handler : store.values()) {
			if (handler.caseAnastomotic(resource)) {
				resource = handler.matcherChar(resource);
			}
		}
		return resource;
	}
	
	/**
	 * 对外提供处理器注册
	 * @param handler
	 */
	public static void register(CharMatcherHandler handler) {
		if (store == null) {
			store = Maps.newHashMap();
		}
		if (handler.getTarget() != null) {
			store.put(handler.getTarget(), handler);
		}
	}
}
