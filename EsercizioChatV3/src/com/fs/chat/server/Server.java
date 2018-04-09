package com.fs.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fs.chat.Constants;

/**
 * The chat server, listens to messages and distributes them to the clients, spawining
 * dedicated threads. It holds the complete chat history, to distribute it to newly
 * connected clients.
 * Sends welcome and goodbye messages when users connect and disconnect.
 * @author FS
 *
 */
public class Server implements Runnable {
	
	private Map<Integer, ServerThread> serverThreads;
	private ServerSocket server;
	private Thread thread;
	
	private List<String> completeChatHistory;
	
	/**
	 * Create a server on the port received in input
	 * @param port
	 */
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
	
	/**
	 * Waits for client connections. When it finds one it spawns
	 * a child thread dedicated to it.
	 */
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
	
	/**
	 * Kills the tread
	 */
	private void stop() {
		thread = null;
	}
	
	/**
	 * Removes a client from the connected clients list, identified by the server
	 * thread associated to it, in order not to listen to
	 * its messages and not to distribute messages to it. <br>
	 * A goodbye message is sent to the remaining clients.
	 * @param ID the id of the server thread associated to the client to be removed
	 */
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
	
	/**
	 * Handles a message from a client and distributes it to all the server
	 * threads, to send it to everyone.
	 * @param ID the ID of the server thread
	 * @param input the message to be sent
	 */
	public synchronized void handleMessage(int ID, String input) {
		String message = input;
		// adds message to chat history, to send it to newly connected clients
		completeChatHistory.add(message);
		for (ServerThread sender : serverThreads.values()) {
			sender.send(message);
		}
	}
	
	/**
	 * Spawns a new server thread dedicated to a client. <br />
	 * A welcome message is sent to the chat.
	 * @param socket the socket of the client that is connecting to the server
	 */
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
	
	/**
	 * Starts the main server thread and initializes chat history
	 */
	public void start() {
		completeChatHistory = new ArrayList<>();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public static void main(String[] args) {
		// starting the server
		new Server(Constants.PORT_NUMBER);
	}

}