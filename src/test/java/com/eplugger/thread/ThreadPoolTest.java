package com.eplugger.thread;

import com.eplugger.thread.utils.ThreadPoolManager;
import com.eplugger.thread.utils.WorkRunnable;
import com.eplugger.thread.utils.WorkThreadFactory;
import com.google.common.base.Strings;

public class ThreadPoolTest {
    public static void main(String[] args) {
    	System.out.println("主线程" + Thread.currentThread().getName() + "等待子线程执行完成...");
    	WorkThreadFactory factory = new WorkThreadFactory("子线程");
    	for (int i = 0; i < 100; i++) {
    		ThreadPoolManager.getManager().executeTask(factory.newThread(new WorkRunnable(Strings.repeat(i + "", 3))));
    	}
    	System.out.println("主线程" + Thread.currentThread().getName() + "开始执行...");
    }
}
