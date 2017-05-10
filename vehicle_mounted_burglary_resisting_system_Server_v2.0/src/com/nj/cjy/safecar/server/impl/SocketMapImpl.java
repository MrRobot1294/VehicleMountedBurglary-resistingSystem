package com.nj.cjy.safecar.server.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.nj.cjy.safecar.server.SocketMap;
import com.nj.cjy.safecar.server.UserSocket;

public class SocketMapImpl implements SocketMap{

	private HashMap<String, UserSocket> socketMap;

	public SocketMapImpl() {
		super();
		
		socketMap = new HashMap<>();
	}
	
	public UserSocket serach(String telephoneNumber) {
		return socketMap.get(telephoneNumber);
	}
	
	public boolean has(String telephoneNumber) {
		return socketMap.containsKey(telephoneNumber);
	}
	
	public void add(String telephoneNumber,UserSocket userSocket) {
		socketMap.put(telephoneNumber, userSocket);
	}
	
	public void socketMapDestroy() {
		Iterator<Entry<String, UserSocket>> iterator = socketMap.entrySet().iterator();
		while(iterator.hasNext()) {
			iterator.next().getValue().userSocketDestory();
		}
		
		socketMap = null;
	}
}
