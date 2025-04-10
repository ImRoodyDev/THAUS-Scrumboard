package com.thaus.chatbox.interfaces;

import javafx.stage.Stage;

public interface CtrlSceneInterface {

	public void initialize(Stage stage);
	public void switchScene(String  sceneName);
	public void loadScene(String  sceneName);
	public void closeCurrentScene();
}
