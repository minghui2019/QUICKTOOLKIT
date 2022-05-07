package com.eplugger.thread.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池管理类
 */
public final class ThreadPoolManager {

	/** 线程池对象 */
	private ExecutorService mExecutorService;

	/** 线程池最大并发数量 */
	private static final int MAX_THREAD_TOTAL = 5;

	private static final class ThreadPoolFactory {
		/**
		 * 静态私有内部类实例
		 */
		private static final ThreadPoolManager mThreadPoolManager = new ThreadPoolManager();
	}

	private ThreadPoolManager() {
		initThreadPool();
	}

	/**
	 * 获取线程池管理实例对象
	 * @return 线程池管理实例对象
	 */
	public static ThreadPoolManager getManager() {
		return ThreadPoolFactory.mThreadPoolManager;
	}

	/**
	 * 初始化一个线程池对象
	 */
	private void initThreadPool() {
		// Java通过Executors提供四种线程池
		// 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
		mExecutorService = Executors.newFixedThreadPool(MAX_THREAD_TOTAL);
		// 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
		// mExecutorService = Executors.newCachedThreadPool();
		// 创建一个定长线程池，支持定时及周期性任务执行
		// mExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
		// 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
		// mExecutorService = Executors.newSingleThreadExecutor();
		/*
		 * 在多线程的开发中往往会遇到这种情况：主线程需要知道子线程的运行结果，以便确定如何执行任务
		 * JDK1.5以后就提供了Callable和Future，通过它们可以在任务执行完毕之后得到任务执行结果。
		 */
	}

	/**
	 * 新线程入池
	 * @param run Runnable
	 */
	public void executeTask(Runnable run) {
		mExecutorService.execute(run);	
	}
	
	/**
	 * 新线程入池，等待返回值
	 * @param run Callable
	 * @return Future<T>
	 */
	public <T> Future<T> submitTask(Callable<T> run) {
		return mExecutorService.submit(run);
	}
	
	/**
	 * 对外提供线程池对象
	 * @return
	 */
	public ExecutorService getExecutorService() {
		return mExecutorService;
	}
}
