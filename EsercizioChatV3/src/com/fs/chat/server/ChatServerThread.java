package com.fs.chat.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatServerThread extends Thread {
	
	private Server server;
	private Socket socket;
	private int ID = -1;
	private DataInputStream inStream;
	private ObjectOutputStream outStream;
	private boolean run = true;

	public ChatServerThread(Server server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
		ID = socket.getPort();
	}
	
	public void send(String message) {
		try {
			outStream.writeObject(message);
			outStream.flush();
		} catch (IOException e) {
			server.remove(ID);
			System.out.println(ID + " error sending");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("server thread " + ID + " running.");
		while (run) {
			try {
				server.handle(ID, inStream.readUTF());
			} catch (IOException e) {
				server.remove(ID);
				run = false;
			}
		}
	}

	public void open() throws IOException {
		inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		outStream = new ObjectOutputStream(socket.getOutputStream());
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (inStream != null)
			inStream.close();
	}
	
	public int getID() {
		return this.ID;
	}
}
