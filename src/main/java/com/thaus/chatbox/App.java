package com.thaus.chatbox;

import com.thaus.chatbox.controllers.SceneController;
import com.thaus.chatbox.controllers.UserController;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
	// Application scene controller
	private SceneController sceneController;

	// Application user controller
	private UserController userController;

	// Start the application process
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Set user controller controller
		userController = new UserController();

		// Create SceneController and pass the Stage explicitly
		sceneController = new SceneController(stage);

		// Set scene controller authentication controller
		sceneController.setUserController(userController);

		// Initialize screens
		sceneController.initialize(1280, 720, false);
	}

	@Override
	public void stop() throws Exception {
		SceneController.closeStage();
		super.stop();
	}

}