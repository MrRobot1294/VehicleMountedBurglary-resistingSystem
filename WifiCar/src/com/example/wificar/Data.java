package com.example.wificar;

import java.net.Socket;

public class Data {
	private static Socket socket;

	public static void setSocket(Socket socket) {
		Data.socket = socket;
	}
	public static Socket getSocket() {
		return socket;
	}

}
