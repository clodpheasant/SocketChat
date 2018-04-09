package com.fs.chat.client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * The spawned thread that listens to new messages
 * @author FS
 *
 */
public class ClientThread extends Thread {

	private Socket socket;
	private Client client;
	private ObjectInputStream inStream;
	private boolean run = true;
	
	/**
	 * Creates and runs the new thread.
	 * @param client the parent client that spawned the server
	 * @param socket the socket that receives the message
	 */
	public ClientThread(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
		
		this.open();
		this.start();
	}
	
	/**
	 * Opens up the input stream.
	 */
	public void open() {
		try {
			inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			client.stop();
			System.out.println("error getting input stream");
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes open resources
	 */
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
	
	/**
	 * Listens to the input stream for new messages.
	 */
	public void run() {
		while (run) {
			try {
				// pass the read message to the client proper
				client.handleMessage((String) inStream.readObject());
			} catch (IOException e) {
				client.stop();
			} catch (ClassNotFoundException e) {
				System.out.println("error handling input class");
				e.printStackTrace();
			}
		}
	}
	
}
