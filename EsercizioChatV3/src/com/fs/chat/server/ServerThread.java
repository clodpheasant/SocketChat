package com.fs.chat.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The spawned server thread dedicated to a specific client
 * @author FS
 *
 */
public class ServerThread extends Thread {
	
	private Server server;
	private Socket socket;
	private int ID = -1;
	private DataInputStream inStream;
	private ObjectOutputStream outStream;
	private boolean run = true;
	
	/**
	 * Starts up the thread. Its unique identifier is the port of the incoming socket
	 * @param server the main server
	 * @param socket the connecting socket
	 */
	public ServerThread(Server server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
		ID = socket.getPort();
	}
	
	/**
	 * Sends a message through the socket, to the client
	 * @param message the message to be send
	 */
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
	
	/**
	 * Main loop, passes to the server what it receives through the socket. <br />
	 * If the connection fails the thread is killed and removed from the main server list.
	 */
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
	
	/**
	 * Opens input and output streams.
	 * @throws IOException
	 */
	public void open() throws IOException {
		inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		outStream = new ObjectOutputStream(socket.getOutputStream());
	}
	
	/**
	 * Closes the socket and the streams.
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (inStream != null)
			inStream.close();
		if (outStream != null)
			outStream.close();
	}
	
	public int getID() {
		return this.ID;
	}
}
