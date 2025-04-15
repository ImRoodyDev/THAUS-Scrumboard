package com.thaus.chatbox.controllers;


import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.views.HomeController;
import com.thaus.chatbox.components.views.LoginRegisterController;
import com.thaus.chatbox.types.ScreenName;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {
	// Array of cached scenes
	private static final Map<ScreenName, Scene> sceneCache = new HashMap<>();
	// Primary stage object
	private static Stage primaryStage;
	// Scene is maximized
	private static boolean isMaximize = false;
	// Current view index
	private static String currentView = "";

	// Central controller
	private static ChatController chatController;
	private static UserController userController;

	// Constructor that receives the Stage from App.java
	public SceneController(Stage stage) {
		if (stage == null) {
			throw new NullPointerException("(Scene controller) Stage cannot be null");
		}

		// Set the primary stage
		primaryStage = stage;
		primaryStage.setMaximized(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		System.out.println("Scene controller created");
	}

	// Function to get the chat Controller
	public static ChatController getChatController() {
		return chatController;
	}

	// Function to set the chat Controller
	public void setChatController(ChatController controller) {
		chatController = controller;
	}

	// Function to get the authentication Controller
	public static UserController getAuthenticationController() {
		return userController;
	}

	// Function to set the authentication Controller
	public void setAuthenticationController(UserController controller) {
		userController = controller;
	}

	// Load the scenes and cache it
	private static Scene loadScene(ScreenName sceneName) throws IOException {
		// Loader for FXML
		FXMLLoader root;
		// Scene for the node
		Scene scene;

		// Creating the scene object
		switch (sceneName) {
			case Home:
				root = new FXMLLoader(SceneController.class.getResource("/components/views/home.fxml"));

				// Load the FXML and set the scene
				scene = new Scene(root.load(), Color.TRANSPARENT);

				// Access the AppController and set the SceneController
				HomeController homeController = root.getController();

				scene.getProperties().put("controller", homeController);
				break;
			case Authentication:
				root = new FXMLLoader(SceneController.class.getResource("/components/views/login-register.fxml"));
				// Load the FXML and set the scene
				scene = new Scene(root.load(), Color.TRANSPARENT);
				// Access the AppController and set the SceneController
				LoginRegisterController loginRegisterController = root.getController();
				scene.getProperties().put("controller", loginRegisterController);
				break;
			default:
				throw new IllegalArgumentException("Scene not found: " + sceneName);
		}

		// Return scene
		return scene;
	}

	// Get stage
	public static Stage getPrimaryStage() {
		return SceneController.primaryStage;
	}

	// Get isMaximized
	public static boolean isMaximized() {
		return SceneController.isMaximize;
	}

	// Minimize stage
	public static void minimize() {
		primaryStage.setIconified(true);

	}

	// Maximize stage
	public static void toggleMaximize() {
		primaryStage.setMaximized(!isMaximize);
		isMaximize = !isMaximize;
	}

	// Close stage
	public static void closeStage() {
		primaryStage.close();
	}

	// Switch between stages
	public static void switchStage(ScreenName screenName) {
		Platform.runLater(() -> {
					try {
						// Check if scene is already cached, if not create it
						if (!sceneCache.containsKey(screenName)) {
							sceneCache.put(screenName, loadScene(screenName));
						}

						// Current Scene
						Scene currentScene = sceneCache.get(screenName);

						// Set current view index
						currentView = screenName.getDisplayName();
						System.out.println("Current stage : " + currentView);

						// Get current sceneBaseController
						BaseScene sceneBaseController = (BaseScene) currentScene.getProperties().get("controller");

						// Set it into the stage
						SceneController.primaryStage.setScene(currentScene);
						SceneController.primaryStage.show();

						// Notify that view is opened
						if (sceneBaseController != null)
							sceneBaseController.onOpen();


					} catch (Exception e) {
						System.out.println("Error occurred loading stage: " + screenName.getDisplayName());
						// e.printStackTrace();
					}
				}
		);
	}

	// Initialize the scene with the given dimensions
	public void initialize(double width, double height, boolean isMaximize) throws Exception {
		// Set window width and height
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);

		// Whether to maximize the window
		SceneController.isMaximize = isMaximize;
		primaryStage.setMaximized(isMaximize);

		primaryStage.setMinHeight(720);
		primaryStage.setMinWidth(1280);

		// Initialize scenes
		this.preloadScenes();

		// Go to the main app if logged in (but for now leave it at true)
		if (userController.isLoggedIn()) {
			SceneController.switchStage(ScreenName.Home);
		} else {
			SceneController.switchStage(ScreenName.Authentication);
		}
	}

	// Preloading the scene and add it in the cache for fast scene loading
	private void preloadScenes() throws IOException {
		// sceneCache.put("Dashboard", loadScene("Dashboard"));
	}
}
