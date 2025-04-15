package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.classes.UserStory;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SprintChat extends VBox implements ICustomNode {
	private final Epic currentUserStoryEpic;
	private final Feature currentUserStoryFeature;
	private final UserStory currentUserStory;

	// General components
	@FXML
	private Label featureLabel;
	@FXML
	private Label epicLabel;
	@FXML
	private Label userStoryLabel;
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
	public SprintChat(Epic epic, Feature feature, UserStory userStory) {
		this.currentUserStoryEpic = epic;
		this.currentUserStoryFeature = feature;
		this.currentUserStory = userStory;
		initializeFXML("/components/tabs/story-chat.fxml");
	}

	public void initialize() {
		// Initialize the chat window
		initializeLabels();
		initializeChat();
	}

	private void initializeLabels() {
		// Set the labels for the chat
		featureLabel.setText(currentUserStoryFeature.getName());
		epicLabel.setText(currentUserStoryEpic.getName());
		userStoryLabel.setText(currentUserStory.getName());
	}

	private void initializeChat() {
		// Add listener to update UI when new messages are added
		currentUserStory.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		currentUserStory.getMessages().forEach((message) -> {
			addMessageToContainer(message);
		});

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			Message message = new Message(messageText);
			currentUserStory.addMessage(message);
			textArea.clear();
		}
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		currentUserStory.getMessages().removeListener(messageListener);
	}
}