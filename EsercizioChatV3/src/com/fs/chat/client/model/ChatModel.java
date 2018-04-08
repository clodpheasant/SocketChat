package com.fs.chat.client.model;

import java.io.IOException;

import com.fs.chat.Constants;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The model handles setting up the data and giving it to the controller, to be shown in the UI.
 * @author FS
 *
 */
public class ChatModel {
	
	private Client client;
	private String name;

	private final ObservableList<String> chatHistory;
	
	/**
	 * Initializes the model, starting up the client
	 * @param hostName where the client connects to
	 * @param name name displayed in the chat
	 * @throws IOException
	 */
	public ChatModel(String hostName, String name) throws IOException {
		this.name = name;
		client = new Client(hostName, Constants.PORT_NUMBER, this);
		chatHistory = FXCollections.observableArrayList();
	}
	
	public Client getClient() {
		return client;
	}
	
	/**
	 * Adds a message to the client's local chat history.
	 * @param message the message to be added
	 */
	public void updateChatHistory(String message) {
		// forces updates on the javafx application thread, in order to avoid
		// IllegalStateException s
		Platform.runLater(() -> {
			chatHistory.add(message);
		});
	}
	
	public ObservableList<String> getChatHistory() {
		return chatHistory;
	}
	
	public String getName() {
		return name;
	}

}
