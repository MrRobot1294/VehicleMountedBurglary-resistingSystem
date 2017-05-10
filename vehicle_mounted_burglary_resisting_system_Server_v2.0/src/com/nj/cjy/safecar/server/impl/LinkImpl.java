package com.nj.cjy.safecar.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.nj.cjy.safecar.server.Link;

public class LinkImpl implements Link {

	private Socket linkSocket;
	private BufferedReader linkReader;
	private PrintWriter linkWriter;
	
	public void setLink(Socket socket, BufferedReader reader, PrintWriter writer) {
		linkSocket = socket;
		linkReader = reader;
		linkWriter = writer;
	}
	
	public Socket getLinkSocket() {
		return linkSocket;
	}

	public void setLinkSocket(Socket linkSocket) {
		this.linkSocket = linkSocket;
	}

	public BufferedReader getLinkReader() {
		return linkReader;
	}

	public void setLinkReader(BufferedReader linkReader) {
		this.linkReader = linkReader;
	}

	public PrintWriter getLinkWriter() {
		return linkWriter;
	}

	public void setLinkWriter(PrintWriter linkWriter) {
		this.linkWriter = linkWriter;
	}

	public void linkDestroy() {
		try {
			linkReader.close();
			linkWriter.close();
			linkSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
