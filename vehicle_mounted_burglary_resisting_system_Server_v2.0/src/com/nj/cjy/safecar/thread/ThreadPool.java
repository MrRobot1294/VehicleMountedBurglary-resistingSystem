package com.nj.cjy.safecar.thread;

public interface ThreadPool {

	/**
	 * Ϊ�̳߳ش�������
	 * @param runnable
	 */
	public void execute(Runnable runnable);
	
	/**
	 * spring�����ر�ʱ�ر��̳߳�
	 */
	public void threadPoolClose();
}
