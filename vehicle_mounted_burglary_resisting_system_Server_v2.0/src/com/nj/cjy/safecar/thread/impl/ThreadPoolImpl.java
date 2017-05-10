package com.nj.cjy.safecar.thread.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nj.cjy.safecar.thread.ThreadPool;

public class ThreadPoolImpl implements ThreadPool{

	private ExecutorService cachedThreadPool;

	public ThreadPoolImpl() {
		super();
		
		cachedThreadPool = Executors.newCachedThreadPool();
	}
	
	public void execute(Runnable runnable) {
		cachedThreadPool.execute(runnable);
	}
	
	public void threadPoolClose() {
		cachedThreadPool.shutdown();
		cachedThreadPool.shutdownNow();
	}
}
