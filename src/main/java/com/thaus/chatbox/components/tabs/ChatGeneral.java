package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ChatGeneral extends VBox implements ICustomNode {

	// General components
	@FXML
	private JFXButton sendMessageBtn;
	@FXML
	private JFXTextArea textArea;
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
	private Group group;

	public ChatGeneral(Group chat) {
		this.group = chat;
		initializeFXML("/components/tabs/chat-general.fxml");
	}

	@FXML
	public void initialize() {
		// Add listener to update UI when new messages are added
		group.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		group.getMessages().forEach((message) -> {
			addMessageToContainer(message);
		});

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			Message message = new Message(messageText);
			group.addMessage(message);
			textArea.clear();
		}
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		group.getMessages().removeListener(messageListener);
	}
}
