package com.fs.chat.client.view;

import com.fs.chat.Constants;
import com.fs.chat.client.model.ChatModel;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * Handles the data provided by the model and shows it on the UI of the chat window.
 * Renders messages according to their type
 * @author FS
 *
 */
public class ChatWindowController {

	private ChatModel model;

	@FXML
	private ListView<String> chatHistory;

	@FXML
	private TextField messageText;
	
	/**
	 * Hooks up the model.
	 * @param model
	 */
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
							
							// the message gets a different color according to its type
							if (message.equals(Constants.WELCOME)) {
								text.setFill(Color.GREEN);
							} else if (message.equals(Constants.GOODBYE)) {
								text.setFill(Color.DARKORANGE);
							} else if (message.equals(Constants.NO_CONNECTION)) {
								text.setFill(Color.RED);
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
	
	/**
	 * When the send button is pressed the message is passed to the client,
	 * that sends it to the server. Scrolls to the bottom of the chat.
	 */
	@FXML
	private void send() {
		String msg = model.getName() + ": " + messageText.getText();
		model.getClient().send(msg);
		messageText.setText("");
		chatHistory.scrollTo(model.getChatHistory().size());
	}
	
	/**
	 * Closes spawned threads in order to quit the application correctly.
	 */
	public void closeThreads() {
		if (model.getClient() != null) {
			model.getClient().stop();
		}
	}

}
