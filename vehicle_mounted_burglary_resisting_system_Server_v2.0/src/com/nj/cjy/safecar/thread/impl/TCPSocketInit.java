package com.nj.cjy.safecar.thread.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.jws.soap.SOAPBinding.Use;

import org.springframework.context.ApplicationContext;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.User;
import com.nj.cjy.safecar.server.Link;
import com.nj.cjy.safecar.server.SocketMap;
import com.nj.cjy.safecar.server.UserSocket;
import com.nj.cjy.safecar.service.CarService;
import com.nj.cjy.safecar.service.UserService;
import com.nj.cjy.safecar.thread.ThreadPool;

public class TCPSocketInit implements Runnable {

	private String number;
	private Link link;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String string = null;
		ThreadPool threadPool;

		int flag_break = 0;

		try {
			while ((string = link.getLinkReader().readLine()) != null) {
				System.out.println(string);
				switch (string.charAt(0)) {
				case 'E': {
					number = string.substring(1, 6);

					CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");

					User user;
					Car car;
					try {
						car = carService.carIsBe(number);

						if ((user = car.getHost()) != null) {
							SocketMap socketMap = (SocketMap) SpringContext.getSpringContext().getBean("SocketMapImpl");
							UserSocket userSocket = (UserSocket) SpringContext.getSpringContext().getBean("UserSoket");
							if (socketMap.has(user.getTelePhoneNumber())) {
								userSocket = socketMap.serach(user.getTelePhoneNumber());
								userSocket.equipmentLogin(number, link);

							} else {
								// UserSocket userSocket = (UserSocket)
								// SpringContext.getSpringContext()
								// .getBean("UserSoket");
								userSocket.telephoneAdd(user.getTelePhoneNumber());
								userSocket.equipmentLogin(number, link);
								socketMap.add(user.getTelePhoneNumber(), userSocket);

								// UserServiceThread userServiceThread =
								// (UserServiceThread) SpringContext
								// .getSpringContext().getBean("UserServiceThread");
								// userServiceThread.setUserSocket(userSocket);
								// threadPool = (ThreadPool)
								// SpringContext.getSpringContext().getBean("ThreadPool");
								// threadPool.execute(userServiceThread);
							}

							SocketThread socketThread = (SocketThread) SpringContext.getSpringContext()
									.getBean("SocketThread");
							socketThread.setSocket("E", number, userSocket);
							threadPool = (ThreadPool) SpringContext.getSpringContext().getBean("ThreadPool");
							System.out.println("登录成功");
							threadPool.execute(socketThread);
							flag_break = 1;
						}
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
					break;

				case 'U': {
					String[] strings = string.split(";");
					number = strings[1];

					String password = strings[2];

					UserService userService = (UserService) SpringContext.getSpringContext().getBean("UserServiceImpl");
					try {
						User user = userService.userIsBe(number, password);
						link.getLinkWriter().println(user.toString());
						link.getLinkWriter().flush();

						SocketMap socketMap = (SocketMap) SpringContext.getSpringContext().getBean("SocketMapImpl");
						UserSocket userSocket = null;
						if (socketMap.has(user.getTelePhoneNumber())) {
							userSocket = socketMap.serach(user.getTelePhoneNumber());
							userSocket.userSignIn(link);
						} else {
							userSocket = (UserSocket) SpringContext.getSpringContext().getBean("UserSoket");
							userSocket.telephoneAdd(number);
							userSocket.userSignIn(link);
							socketMap.add(number, userSocket);
						}
						SocketThread socketThread = (SocketThread) SpringContext.getSpringContext()
								.getBean("SocketThread");
						socketThread.setSocket("U", number, userSocket);
						threadPool = (ThreadPool) SpringContext.getSpringContext().getBean("ThreadPool");
						threadPool.execute(socketThread);
						flag_break = 1;
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						link.getLinkWriter().println("USER_ERROR");
						link.getLinkWriter().flush();
					}
				}
					break;
				case 'R': {
					String[] strings = string.split(";");
					number = strings[1];
					
					UserService userService = (UserService) SpringContext.getSpringContext().getBean("UserServiceImpl");
					try {
						userService.userSignUp(strings[3], strings[1], strings[2], "user");
						
						link.getLinkWriter().println("REGISTER_SUCCESS");
						link.getLinkWriter().flush();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						link.getLinkWriter().println("REGISTER_ERROR");
						link.getLinkWriter().flush();
					}
				}
					break;
				default: {
					string = null;
				}
					break;
				}
				if (flag_break == 1) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (number == null) {
				System.out.println("获取设备号失败");
				link.linkDestroy();
				number = null;
			}
		}
	}

	/**
	 * set equipment's socket open BufferedReader return true if fail return
	 * false
	 */
	public boolean setSocket(Socket socket) {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.link = (Link) SpringContext.getSpringContext().getBean("Link");
			this.link.setLink(socket, reader, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			return false;
		}
		return true;
	}

}
