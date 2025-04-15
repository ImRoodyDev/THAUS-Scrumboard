package com.thaus.chatbox.interfaces;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public interface ICustomNode {

	default void initializeFXML() {
	}

	default void initializeFXML(String path) {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML: " + path, e);
		}
	}
}
