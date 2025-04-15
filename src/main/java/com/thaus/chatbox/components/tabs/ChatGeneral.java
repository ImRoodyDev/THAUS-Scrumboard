package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.thaus.chatbox.classes.Chat;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ChatGeneral extends VBox implements ICustomNode {

	// General components
	@FXML
	private JFXButton sendMessage;
	@FXML
	private JFXTextField textArea;
	@FXML
	private VBox messageContainer;
	private final ListChangeListener<Message> messageListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Message message : change.getAddedSubList()) {
					addMessageToContainer(message);
				}
			}
		}
	};
	// Private's
	private Chat chat;

	public ChatGeneral(Chat chat) {
		this.chat = chat;
		initializeFXML("/components/tabs/chat-general.fxml");

	}

	@FXML
	public void initialize() {
		// Add listener to update UI when new messages are added
		chat.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		chat.getMessages().forEach(this::addMessageToContainer);

		// Set up send button action
		sendMessage.setOnAction(event -> sendMessage());
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			Message message = new Message(messageText);
			chat.addMessage(message);
			textArea.clear();
		}
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		chat.getMessages().removeListener(messageListener);
	}
}
