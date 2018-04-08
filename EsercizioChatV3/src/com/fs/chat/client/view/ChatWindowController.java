package com.fs.chat.client.view;

import com.fs.chat.client.model.ChatModel;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
		
		// wraps lines inside list view
		chatHistory.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(final ListView<String> list) {
				return new ListCell<String>() {
					{
						Text text = new Text();
						text.wrappingWidthProperty().bind(list.widthProperty().subtract(15));
						text.textProperty().bind(itemProperty());

						setPrefWidth(0);
						setGraphic(text);
					}
				};
			}
		});

		chatHistory.setItems(model.getChatHistory());
	}

	@FXML
	private void send() {
		String msg = model.getName() + ": " + messageText.getText();
		model.getClient().send(msg);
		messageText.setText("");
	}

}
