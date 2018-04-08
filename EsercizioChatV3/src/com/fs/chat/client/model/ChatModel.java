package com.fs.chat.client.model;

import java.io.IOException;
import java.net.InetAddress;

import com.fs.chat.Constants;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {
	
	private Client client;

	private final ObservableList<String> chatHistory;
	
	public ChatModel() throws IOException {
		String hostName = InetAddress.getLocalHost().getHostName();
		client = new Client(hostName, Constants.PORT_NUMBER, this);
		chatHistory = FXCollections.observableArrayList();
	}
	
	public Client getClient() {
		return client;
	}
	
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

}
