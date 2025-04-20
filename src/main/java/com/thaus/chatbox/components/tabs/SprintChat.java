package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.util.Date;

public class SprintChat extends VBox implements ICustomNode {
	// General components
	@FXML
	private Label sprintLabel;
	@FXML
	private JFXButton sendMessageBtn;
	@FXML
	private JFXTextArea textArea;
	@FXML
	private VBox messageContainer;

	// Observable properties
	private String groupId;
	private String sprintId;
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
	public SprintChat() {
		initializeFXML("/components/tabs/sprint-chat.fxml");
	}

	public void initialize() {
		this.groupId = UserController.getChatController().currentGroup().getId();
		Sprint currentSprint = UserController.getChatController().currentSprint();
		this.sprintId = currentSprint.getId();

		// Initialize the chat window
		initializeLabels(currentSprint);
		initializeChat(currentSprint);
	}

	private void initializeLabels(Sprint currentSprint) {
		// Set the labels for the chat
		sprintLabel.textProperty().bind(currentSprint.getName());
	}

	private void initializeChat(Sprint currentSprint) {
		// Add listener to update UI when new messages are added
		currentSprint.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		currentSprint.getMessages().forEach(this::addMessageToContainer);

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			Message message = new Message(messageText);

			JSONObject response = UserController.sendSprintMessage(
					groupId,
					sprintId,
					messageText
			);

			if (response == null || response.getInt("statusCode") > 203) {
				// Handle error
				System.out.println("Error sending message: " + response);
				return;
			} else {
				Sprint currentSprint = UserController.getChatController().currentSprint();
				currentSprint.addMessage(
						new Message(
								UserController.getCurrentUser().getUsername().get(),
								messageText,
								new Date(),
								true
						)
				);

				// Clear the text area after sending the message
				textArea.clear();
			}
		}
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		sprintLabel.textProperty().unbind();
		sendMessageBtn.setOnAction(null);
		messageContainer.getChildren().clear();
		Sprint currentSprint = UserController.getChatController().currentSprint();
		currentSprint.getMessages().removeListener(messageListener);
	}
}