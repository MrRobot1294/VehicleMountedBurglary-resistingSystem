package com.nj.cjy.safecar.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.server.TCPServer;
import com.nj.cjy.safecar.thread.ThreadPool;
import com.nj.cjy.safecar.thread.impl.TCPSocketInit;
import com.nj.cjy.safecar.thread.impl.ThreadPoolImpl;

public class TCPServerImpl implements TCPServer{

	private ServerSocket tcpServer;

	public TCPServerImpl() {
		super();
		try {
			this.tcpServer = new ServerSocket(XXXX);
			System.out.println("XXXX");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TCPServerImpl(int port) {
		super();
		try {
			this.tcpServer = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tcpaccept() {
		ThreadPool threadPool = (ThreadPool)SpringContext.getSpringContext().getBean("ThreadPool");
		while (true) {
			try {
				Socket socket = tcpServer.accept();
				System.out.println("设备连接中");

				TCPSocketInit socketInit = (TCPSocketInit) SpringContext.getSpringContext().getBean("TCPSocketInit");
				socketInit.setSocket(socket);
				
				threadPool.execute(socketInit);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void serverDestroy() {
		try {
			tcpServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
