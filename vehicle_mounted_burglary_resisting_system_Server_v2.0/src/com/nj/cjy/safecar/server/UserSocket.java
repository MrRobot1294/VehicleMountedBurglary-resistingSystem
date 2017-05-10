package com.nj.cjy.safecar.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public interface UserSocket {
	public void setTelephoneNumber(String telephoneNumber);


	public void setCount(int count);


	public void setUserLink(Link userLink);
	
	public String getTelephoneNumber();


	public int getCount();


	public Link getUserLink();


	public HashMap<String, Link> getCarSocket();

	public void telephoneAdd(String tString);
	
	public void userSignIn(Link uLink);
	
	public void equipmentLogin(String number, Link eLink);
	
	public void userSocketDestory();
}
