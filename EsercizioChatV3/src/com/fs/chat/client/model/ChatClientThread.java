package com.fs.chat.client.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread {

	private Socket socket;
	private Client client;
	private DataInputStream inStream;
	private boolean run = true;
	
	public ChatClientThread(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
		this.open();
		this.start();
	}
	
	public void open() {
		try {
			inStream = new DataInputStream(socket.getInputStream());
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
				client.handle(inStream.readUTF());
			} catch (IOException e) {
				client.stop();
				System.out.println("error listening");
				e.printStackTrace();
			}
		}
	}
	
}
