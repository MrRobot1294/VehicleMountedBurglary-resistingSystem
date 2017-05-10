package com.nj.cjy.safecar.server;

public interface SocketMap {

	
	public UserSocket serach(String telephoneNumber);
	public void add(String telephoneNumber,UserSocket userSocket);
	public boolean has(String telephoneNumber);
	
	public void socketMapDestroy();
}
