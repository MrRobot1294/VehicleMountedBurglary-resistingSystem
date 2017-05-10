package com.nj.cjy.safecar.server;

public interface TCPServer {

	/**
	 * 接收tcp连接
	 */
	public void tcpaccept();
	
	/**
	 * spring容器关闭时关闭socketserver
	 */
	public void serverDestroy();
}
