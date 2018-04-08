package com.fs.chat.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {

	private Socket socket;
	private Client client;
	private ObjectInputStream inStream;
	private boolean run = true;
	
	public ClientThread(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
		
		this.open();
		this.start();
	}
	
	public void open() {
		try {
			inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			client.stop();
			System.out.println("error getting input stream");
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (inStream != null) {
				inStream.close();
			}
			run = false;
		} catch (IOException e) {
			System.out.println("error closing input stream");
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (run) {
			try {
				client.handle((String) inStream.readObject());
			} catch (IOException e) {
				client.stop();
			} catch (ClassNotFoundException e) {
				System.out.println("error handling input class");
				e.printStackTrace();
			}
		}
	}
	
}
