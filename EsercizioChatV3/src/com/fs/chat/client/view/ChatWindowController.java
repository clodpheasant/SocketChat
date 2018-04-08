package com.fs.chat.client.view;

import com.fs.chat.Constants;
import com.fs.chat.client.model.ChatModel;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
		
		// text formatting operations: wrapping lines and changing color
		chatHistory.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(final ListView<String> list) {
				return new ListCell<String>() {
					
					@Override
					public void updateItem(String message, boolean empty) {
						super.updateItem(message, empty);
						if (!empty && message != null) {

							Text text = new Text();
							text.wrappingWidthProperty().bind(list.widthProperty().subtract(20));
							text.textProperty().bind(itemProperty());
							
							if (message.equals(Constants.WELCOME)) {
								text.setFill(Color.GREEN);
							} else if (message.equals(Constants.GOODBYE)) {
								text.setFill(Color.DARKORANGE);
							} else {
								int endOfName = message.indexOf(':');
								if (endOfName >= 0) {
									if (message.substring(0, endOfName).equals(model.getName())) {
										text.setFill(Color.CADETBLUE);
									}
								}
							}
							
							setPrefWidth(0);
							setGraphic(text);
						}
					}
				};
			}
		});
		
		// rows are non traversable
		chatHistory.setFocusTraversable(false);
		// scrolls to the bottom
		chatHistory.scrollTo(model.getChatHistory().size());
		// populates listview
		chatHistory.setItems(model.getChatHistory());
	}

	@FXML
	private void send() {
		String msg = model.getName() + ": " + messageText.getText();
		model.getClient().send(msg);
		messageText.setText("");
	}
	
	public void closeThreads() {
		model.getClient().stop();
	}

}
