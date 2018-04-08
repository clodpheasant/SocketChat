package com.fs.chat.client;

import java.io.IOException;
import java.net.URISyntaxException;

import com.fs.chat.Constants;
import com.fs.chat.client.model.ChatModel;
import com.fs.chat.client.view.ChatWindowController;
import com.fs.chat.server.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Simple chat, handles multiple clients connecting to the same {@link Server}.
 * Past chat history is retrieved when the user connects, it's not possible to
 * edit or remove past messages.
 * 
 * @author FS
 *
 */
public class ChatApp extends Application {
	
	private ChatModel model;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		// giving a random name to the user
		String name = Constants.getRandomName();
		
		// by default, the server is hosted on localhost, so the clients connect there
		String hostName = Constants.getHostName();
		
		// setting up the stage
		primaryStage.setTitle("Chat - " + name);

		// root to attach the various elements of the UI
		BorderPane root = new BorderPane();

		// loading the FXML main view
		FXMLLoader chatLoader = new FXMLLoader();
		chatLoader.setLocation(ChatApp.class.getResource("view/ChatWindow.fxml"));
		root.setCenter(chatLoader.load());
		
		// getting the controllers and hooking up the model
		ChatWindowController chatWindowController = chatLoader.getController();
		model = new ChatModel(hostName, name);
		chatWindowController.initModel(model);

		// setting up the scene
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		// action to be triggered when window is closed
		primaryStage.setOnCloseRequest(e -> chatWindowController.closeThreads());
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
