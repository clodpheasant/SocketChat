package com.fs.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.fs.chat.Constants;

public class Server implements Runnable {
	
	private ChatServerThread[] serverThreads = new ChatServerThread[50];
	private ServerSocket server;
	private Thread thread;
	private int clientsCount = 0;
	
	private List<String> completeChatHistory;
	
	public Server(int port) {
		try {
			System.out.println("binding to port: " + port);
			server = new ServerSocket(port);
			System.out.println("server started: " + server);
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
	
	private int findClient(int ID) {
		for (int i = 0; i < clientsCount; i++) {
			if (serverThreads[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}
	
	public synchronized void remove(int ID) {
		int i = findClient(ID);
		if (i >= 0) {
			ChatServerThread toBeRemoved = serverThreads[i];
			System.out.println("removing client " + ID + " at " + i);
			if (i > clientsCount - 1) {
				for (int j = i + 1; j < clientsCount; j++) {
					serverThreads[j-1] = serverThreads[j];
				}
			}
			clientsCount--;
			try {
				toBeRemoved.close();
			} catch (IOException e) {
				System.out.println("error closing");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void handle(int ID, String input) {
		if (input.equals("bye")) {
			serverThreads[findClient(ID)].send("bye");
			remove(ID);
		} else {
			String msg = input;
			// adds message to chat history, to send it to newly connected clients
			completeChatHistory.add(msg);
			for (int i = 0; i < clientsCount; i++) {
				serverThreads[i].send(msg);
			}
		}
	}
	
	private void addThread(Socket socket) {
		if (clientsCount < serverThreads.length) {
			System.out.println("client accepted: " + socket);
			ChatServerThread serverThread = new ChatServerThread(this, socket);
			serverThreads[clientsCount] = serverThread;
			try {
				serverThread.open();
				serverThread.start();
				clientsCount++;
				// sends all messages from history
				for (String msg : completeChatHistory) {
					serverThread.send(msg);
				}
			} catch (IOException e) {
				System.out.println("error opening thread");
				e.printStackTrace();
			}
		} else {
			System.out.println("client refused: maximum of " + serverThreads.length + "reached");
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