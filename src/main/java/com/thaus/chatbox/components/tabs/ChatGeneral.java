package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.util.Date;

public class ChatGeneral extends VBox implements ICustomNode {

	// General components
	@FXML
	private JFXButton sendMessageBtn;
	@FXML
	private JFXTextArea textArea;
	@FXML
	private VBox messageContainer;

	// Observable components
	private Group group;
	private final ListChangeListener<Message> messageListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Message message : change.getAddedSubList()) {
					addMessageToContainer(message);
				}
			}
		}
	};

	public ChatGeneral() {
		initializeFXML("/components/tabs/chat-general.fxml");
	}

	@FXML
	public void initialize() {
		loadGroup(UserController.getChatController().currentGroup());
	}

	public void loadGroup(Group group) {
		// Cleanup just in case if a group has been added
		cleanup();
		this.group = group;

		// Add listener to update UI when new messages are added
		group.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		group.getMessages().forEach(this::addMessageToContainer);

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {
			sendMessageBtn.setDisable(true);
			JSONObject response = UserController.sendMessage(group.getId(), messageText);

			if (response == null || response.getInt("statusCode") > 203) {
				System.out.println("Error sending message");
			} else {
				// Create a new message object
				Message message = new Message(
						UserController.getCurrentUser().getUsername().get(),
						messageText,
						new Date(),
						true
				);

				// Handle success
				System.out.println("Message sent successfully");
				group.addMessage(message);
				textArea.clear();
			}

			// Clear the text area after sending the message
			sendMessageBtn.setDisable(false);
		}
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		if (group != null) {
			group.getMessages().removeListener(messageListener);
			group = null;
		}
		// Clear the message container
		messageContainer.getChildren().clear();
		sendMessageBtn.setOnAction(null);
	}
}
