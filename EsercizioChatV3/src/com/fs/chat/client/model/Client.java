package com.fs.chat.client.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket socket;
	private Thread thread;
	private BufferedReader inStream;
	private DataOutputStream outStream;
	private ClientThread clientThread;
	
	private ChatModel model;
	
	public Client(String serverName, int serverPort, ChatModel model) {
		this.model = model;
		System.out.println("connecting. please wait...");
		try {
			socket = new Socket(serverName, serverPort);
			System.out.println("connected: " + socket);
			this.start();
		} catch (UnknownHostException e) {
			System.out.println("unknown host.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("unexpected exception.");
			e.printStackTrace();
		}
	}
	
	public void send(String message) {
		try {
			outStream.writeUTF(message);
			outStream.flush();
		} catch (IOException ioe) {
			this.stop();
		}
	}
	
	public void handle(String message) {
		if (message.equals("bye")) {
			System.out.println("buh bye - hit return to exit");
			this.stop();
		} else {
			System.out.println(message);
			model.updateChatHistory(message);
		}
	}
	
	private void start() throws IOException {
		inStream = new BufferedReader(new InputStreamReader(System.in));
		outStream = new DataOutputStream(socket.getOutputStream());
		if (thread == null) {
			clientThread = new ClientThread(this, socket);
		}
	}
	
	public void stop() {
		try {
			if (inStream != null) inStream.close();
			if (outStream != null) outStream.close();
			if (socket != null) socket.close();
		} catch (IOException e) {
			System.out.println("error closing");
			e.printStackTrace();
		}
		clientThread.close();
	}
}