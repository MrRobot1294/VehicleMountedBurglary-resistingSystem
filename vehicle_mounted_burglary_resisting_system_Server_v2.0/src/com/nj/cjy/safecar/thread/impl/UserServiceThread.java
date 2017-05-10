package com.nj.cjy.safecar.thread.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.nj.cjy.safecar.server.Link;
import com.nj.cjy.safecar.server.UserSocket;

public class UserServiceThread implements Runnable {

	private UserSocket userSocket;
	
	public void setUserSocket(UserSocket userSocket) {
		this.userSocket = userSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Link link = null;
		String string = null;
		Entry<String, Link> carLink = null;
		Iterator<Entry<String, Link>> iterator = null;
		while (userSocket.getCount() != 0) {
			if ((link = userSocket.getUserLink()) != null) {
				try {
					if((string = link.getLinkReader().readLine()) != null) {
						
					} else {
						userSocket.setUserLink(null);
						userSocket.setCount(userSocket.getCount() - 1);
						link.linkDestroy();
						link = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			iterator = userSocket.getCarSocket().entrySet().iterator();
			while (iterator.hasNext()) {
				carLink = iterator.next();
				System.out.println(carLink.getKey());
				link = carLink.getValue();
				try {
					if ((string = link.getLinkReader().readLine()) != null) {
						System.out.println(string);
					} else {
						iterator.remove();
						userSocket.setCount(userSocket.getCount() - 1);
						link.linkDestroy();
						link = null;
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
