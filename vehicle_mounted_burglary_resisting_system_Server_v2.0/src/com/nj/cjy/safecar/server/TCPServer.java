package com.nj.cjy.safecar.server;

public interface TCPServer {

	/**
	 * ����tcp����
	 */
	public void tcpaccept();
	
	/**
	 * spring�����ر�ʱ�ر�socketserver
	 */
	public void serverDestroy();
}
