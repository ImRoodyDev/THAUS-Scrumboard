package com.thaus.chatbox.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.controllers.ChatController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChatComponent extends VBox implements ICustomNode {
	private final String chatboxId;
	private final ChatController chatController;

	// Array of messages
	private final ObservableList<MessageComponent> messageComponents = FXCollections.observableArrayList();
	// Components
	public JFXButton threadButton;
	public Label chatLabel;
	public JFXButton addButton;
	public JFXButton searchButton;
	public AnchorPane dynamicContainer;
	public VBox messageContainer;
	public JFXTextArea textArea;
	public JFXButton sendMessage;

	public ChatComponent(ChatController chatController) {
		this.chatController = chatController;
		this.chatboxId = chatController.getSelectedChatId();
		initializeFXML();
	}

	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/chat.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
		}
	}


	private void sendMessage() {
		String text = textArea.getText();
		if (!text.isEmpty()) {
			chatController.sendGeneralMessage(chatboxId, text);
			textArea.clear();
		}
	}
}
