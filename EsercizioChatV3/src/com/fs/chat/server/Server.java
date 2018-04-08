package com.fs.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fs.chat.Constants;

public class Server implements Runnable {
	
	private Map<Integer, ServerThread> serverThreads;
	private ServerSocket server;
	private Thread thread;
	
	private List<String> completeChatHistory;
	
	public Server(int port) {
		try {
			System.out.println("binding to port: " + port);
			server = new ServerSocket(port);
			System.out.println("server started: " + server);
			serverThreads = new HashMap<>();
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (thread != null) {
			try {
				System.out.println("waiting for client...");
				Socket socket = server.accept();
				this.addThread(socket);
			} catch (IOException e) {
				System.out.println("accept failed");
				e.printStackTrace();
				this.stop();
			}
		}
	}
	
	private void stop() {
		thread = null;
	}
	
	public synchronized void remove(int ID) {
		ServerThread toBeRemoved = serverThreads.get(ID);
		if (toBeRemoved != null) {
			System.out.println("removing client " + ID);
			serverThreads.remove(ID);
			try {
				toBeRemoved.close();
			} catch (IOException e) {
				System.out.println("error closing");
				e.printStackTrace();
			}
			
			completeChatHistory.add(Constants.GOODBYE);
			for (ServerThread sender : serverThreads.values()) {
				sender.send(Constants.GOODBYE);
			}
		}
	}
	
	public synchronized void handle(int ID, String input) {
		String msg = input;
		// adds message to chat history, to send it to newly connected clients
		completeChatHistory.add(msg);
		for (ServerThread sender : serverThreads.values()) {
			sender.send(msg);
		}
	}
	
	private void addThread(Socket socket) {
		System.out.println("client accepted: " + socket);
		
		completeChatHistory.add(Constants.WELCOME);
		for (ServerThread sender : serverThreads.values()) {
			sender.send(Constants.WELCOME);
		}
		
		ServerThread tobeAdded = new ServerThread(this, socket);
		serverThreads.put(socket.getPort(), tobeAdded);
		try {
			tobeAdded.open();
			tobeAdded.start();
			
			// sends all messages from history
			for (String msg : completeChatHistory) {
				tobeAdded.send(msg);
			}
		} catch (IOException e) {
			System.out.println("error opening thread");
			e.printStackTrace();
		}
	}
	
	public void start() {
		completeChatHistory = new ArrayList<>();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public static void main(String[] args) {
		new Server(Constants.PORT_NUMBER);
	}

}