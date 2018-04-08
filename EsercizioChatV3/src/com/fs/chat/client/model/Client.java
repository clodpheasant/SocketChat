package com.fs.chat.client.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The client that connects to the server: it creates a socket and spawns a thread
 * that listens to new messages.
 * @author FS
 *
 */
public class Client {
	
	private Socket socket;
	private Thread thread;
	private BufferedReader inStream;
	private DataOutputStream outStream;
	private ClientThread clientThread;
	
	private ChatModel model;
	
	/**
	 * Sets up and starts the client.
	 * 
	 * @param serverName the server the client connects to
	 * @param serverPort the port of the server
	 * @param model the model that handles the data
	 */
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
	
	/**
	 * Sends a message out to the server.
	 * @param message the message to be sent
	 */
	public void send(String message) {
		try {
			outStream.writeUTF(message);
			outStream.flush();
		} catch (IOException ioe) {
			this.stop();
		}
	}
	
	/**
	 * Handles a new message received: it's sent to the model
	 * to be displayed
	 * @param message the message received
	 */
	public void handle(String message) {
		System.out.println(message);
		model.updateChatHistory(message);
	}
	
	/**
	 * Sets up the input and output stream and spawns the listener thread.
	 * @throws IOException
	 */
	private void start() throws IOException {
		inStream = new BufferedReader(new InputStreamReader(System.in));
		outStream = new DataOutputStream(socket.getOutputStream());
		if (thread == null) {
			clientThread = new ClientThread(this, socket);
		}
	}
	
	/**
	 * Closes open resources.
	 */
	public void stop() {
		clientThread.close();
		try {
			if (inStream != null) inStream.close();
			if (outStream != null) outStream.close();
			if (socket != null) socket.close();
		} catch (IOException e) {
			System.out.println("error closing");
			e.printStackTrace();
		}
	}
}