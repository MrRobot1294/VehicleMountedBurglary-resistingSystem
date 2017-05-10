package com.nj.cjy.safecar.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface Link {

	public void setLink(Socket socket, BufferedReader reader, PrintWriter writer);
	
	public void linkDestroy();
	
	public Socket getLinkSocket();

	public void setLinkSocket(Socket linkSocket);

	public BufferedReader getLinkReader();

	public void setLinkReader(BufferedReader linkReader);

	public PrintWriter getLinkWriter();

	public void setLinkWriter(PrintWriter linkWriter);
}
