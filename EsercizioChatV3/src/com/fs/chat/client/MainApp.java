package com.fs.chat.client;

import java.io.IOException;
import java.net.URISyntaxException;

import com.fs.chat.Constants;
import com.fs.chat.client.model.ChatModel;
import com.fs.chat.client.view.ChatWindowController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private ChatModel model;
	
	@Override
	public void start(Stage primaryStage) throws IOException, URISyntaxException {
		String name = Constants.getName();
		
		// setting up the stage
		primaryStage.setTitle("Chat - " + name);

		// root to attach the various elements of the UI
		BorderPane root = new BorderPane();

		// loading the FXML main view
		FXMLLoader chatLoader = new FXMLLoader();
		chatLoader.setLocation(MainApp.class.getResource("view/ChatWindow.fxml"));
		root.setCenter(chatLoader.load());
		
		// getting the controllers and hooking up the model
		ChatWindowController chatWindowController = chatLoader.getController();
		model = new ChatModel(name);
		chatWindowController.initModel(model);

		// setting up the scene
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
