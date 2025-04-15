package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SprintChat extends VBox implements ICustomNode {
	private final Sprint currentSprint;

	// General components
	@FXML
	private Label sprintLabel;
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

	// Constructor
	public SprintChat(Sprint sprint) {
		this.currentSprint = sprint;
		initializeFXML("/components/tabs/sprint-chat.fxml");
	}

	public void initialize() {
		// Initialize the chat window
		initializeLabels();
		initializeChat();
	}

	private void initializeLabels() {
		// Set the labels for the chat
		sprintLabel.setText(currentSprint.getName());
	}

	private void initializeChat() {
		// Add listener to update UI when new messages are added
		currentSprint.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		currentSprint.getMessages().forEach((message) -> {
			addMessageToContainer(message);
		});

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			Message message = new Message(messageText);
			currentSprint.addMessage(message);
			textArea.clear();
		}
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		currentSprint.getMessages().removeListener(messageListener);
	}
}