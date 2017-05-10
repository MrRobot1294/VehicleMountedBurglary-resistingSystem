package com.nj.cjy.safecar.thread;

public interface ThreadPool {

	/**
	 * 为线程池传入任务
	 * @param runnable
	 */
	public void execute(Runnable runnable);
	
	/**
	 * spring容器关闭时关闭线程池
	 */
	public void threadPoolClose();
}
