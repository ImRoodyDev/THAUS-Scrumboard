package com.thaus.chatbox;

import com.thaus.chatbox.controllers.AuthenticationController;
import com.thaus.chatbox.controllers.ChatController;
import com.thaus.chatbox.controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
	// Application scene controller
	private SceneController sceneController;

	// Application chat controller
	private ChatController chatController;

	// Application authentication controller
	private AuthenticationController authenticationController;

	// Start the application process
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Create SceneController and pass the Stage explicitly
		sceneController = new SceneController(stage);

		// Set scene controller chat controller
		sceneController.setChatController(new ChatController());

		// Set scene controller authentication controller
		sceneController.setAuthenticationController(new AuthenticationController());

		// Initialize screens
		sceneController.initialize(1280, 720, false);
	}

	@Override
	public void stop() throws Exception {
		SceneController.closeStage();
		super.stop();
	}

}