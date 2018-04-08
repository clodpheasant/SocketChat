package com.fs.chat.client.view;

import com.fs.chat.client.model.ChatModel;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatWindowController {
	
	private ChatModel model;
	
	@FXML
	private ListView<String> chatHistory;
	
	@FXML
	private TextField messageText;
	
	public void initModel(ChatModel model) {
		// ensure model is only set once:
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}

		this.model = model;
		
		chatHistory.setItems(model.getChatHistory());
	}
	
	@FXML
	private void send() {
		String msg = model.getName() + ": " + messageText.getText();
		model.getClient().send(msg);
		messageText.setText("");
	}

}
