package com.eplugger.thread.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkThreadFactory implements ThreadFactory {
	private String threadName;
	private AtomicInteger threadNum = new AtomicInteger(1);
	
	public WorkThreadFactory(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(this.threadName + "-" + threadNum.toString());
		threadNum.incrementAndGet();
		return thread;
	}
}

