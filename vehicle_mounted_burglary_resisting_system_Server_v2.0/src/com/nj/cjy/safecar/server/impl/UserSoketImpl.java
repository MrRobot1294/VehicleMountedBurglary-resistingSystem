package com.nj.cjy.safecar.server.impl;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.nj.cjy.safecar.server.Link;
import com.nj.cjy.safecar.server.UserSocket;

public class UserSoketImpl implements UserSocket {

	private String telephoneNumber;
	private int count;
	private Link userLink;
	private HashMap<String, Link> carSocket;
	
	public UserSoketImpl() {
		super();
		count = 0;
		carSocket = new HashMap<>();
	}
	
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public void setUserLink(Link userLink) {
		this.userLink = userLink;
	}


	public String getTelephoneNumber() {
		return telephoneNumber;
	}


	public int getCount() {
		return count;
	}


	public Link getUserLink() {
		return userLink;
	}


	public HashMap<String, Link> getCarSocket() {
		return carSocket;
	}


	public void telephoneAdd(String tString) {
		telephoneNumber = tString;
	}
	
	public void userSignIn(Link uLink) {
		if(userLink == null) {
			count++;
		} else {
			userLink.linkDestroy();
		}
		userLink = uLink;
	}
	
	public void equipmentLogin(String number, Link eLink){
		if(carSocket.containsKey(number) == true) {
			Link link = carSocket.get(number);
			carSocket.remove(number);
			link.linkDestroy();
			
			count--;
		}
		carSocket.put(number, eLink);
		count++;
	}
	
	public void userSocketDestory() {
		telephoneNumber = null;
		if (userLink != null) {
			userLink.linkDestroy();
		}
		Iterator<Entry<String, Link>> iterator = carSocket.entrySet().iterator();
		while (iterator.hasNext()) {
			iterator.next().getValue().linkDestroy();
		}
		carSocket = null;
	}
}
