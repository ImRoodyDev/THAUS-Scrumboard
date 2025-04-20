package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.classes.Story;
import com.thaus.chatbox.components.informative.MessageComponent;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.util.Date;

public class StoryChat extends VBox implements ICustomNode {
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

	// Observable properties
	private String groupId;
	private String storyId;
	private Runnable onFeatureClickHandler;
	private Runnable onEpicClickHandler;
	private Runnable onStoryClickHandler;
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
	public StoryChat() {
		initializeFXML("/components/tabs/story-chat.fxml");
	}

	public void initialize() {
		Story currentStory = UserController.getChatController().currentStory();
		Feature storyFeature = UserController.getChatController().currentFeature();
		Epic storyEpic = UserController.getChatController().currentEpic();

		this.groupId = UserController.getChatController().currentGroup().getId();
		this.storyId = currentStory.getId();

		// Initialize the chat window
		initializeLabels(storyFeature, storyEpic, currentStory);
		initializeChat(currentStory);
	}

	private void initializeLabels(Feature storyFeature, Epic storyEpic, Story currentStory) {
		int featureIndex = UserController.getChatController().currentGroup().getFeatures().indexOf(storyFeature);
		int epicIndex = storyFeature.getEpics().indexOf(storyEpic);
		int storyIndex = storyEpic.getStories().indexOf(currentStory);

		// Set the labels for the chat
		featureLabel.setText("Feature " + (featureIndex + 1));
		epicLabel.setText("Epic " + (epicIndex + 1));
		userStoryLabel.setText("User Story " + (storyIndex + 1));
	}

	private void initializeChat(Story currentStory) {
		// Add listener to update UI when new messages are added
		currentStory.getMessages().addListener(messageListener);

		// Clear the message container
		messageContainer.getChildren().clear();

		// Initialize existing messages
		currentStory.getMessages().forEach(this::addMessageToContainer);

		// Set up send button action
		sendMessageBtn.setOnAction(_ -> sendMessage());
	}

	private void sendMessage() {
		String messageText = textArea.getText();
		if (!messageText.isEmpty()) {

			JSONObject response = UserController.sendStoryMessage(
					groupId,
					storyId,
					messageText
			);

			if (response == null || response.getInt("statusCode") > 203) {
				return;
			} else {
				// Add the message to the current story
				Story currentStory = UserController.getChatController().currentStory();
				currentStory.addMessage(new Message(
						UserController.getCurrentUser().getUsername().get(),
						messageText,
						new Date(),
						true
				));

				// Clear the text area after sending the message
				textArea.clear();
			}
		}
	}

	private void addMessageToContainer(Message message) {
		messageContainer.getChildren().add(new MessageComponent(message));
	}

	public void handleFeatureClick(Runnable handler) {
		this.onFeatureClickHandler = handler;
		featureLabel.setOnMouseClicked(_ -> {
			if (onFeatureClickHandler != null) {
				onFeatureClickHandler.run();
			}
		});
	}

	public void handleEpicClick(Runnable handler) {
		this.onEpicClickHandler = handler;
		epicLabel.setOnMouseClicked(_ -> {
			if (onEpicClickHandler != null) {
				onEpicClickHandler.run();
			}
		});
	}

	public void handleStoryClick(Runnable handler) {
		this.onStoryClickHandler = handler;
		userStoryLabel.setOnMouseClicked(_ -> {
			if (onStoryClickHandler != null) {
				onStoryClickHandler.run();
			}
		});
	}

	// Call this method when the ChatGeneral instance is destroyed
	public void cleanup() {
		onEpicClickHandler = null;
		onFeatureClickHandler = null;
		onStoryClickHandler = null;

		epicLabel.setOnMouseClicked(null);
		featureLabel.setOnMouseClicked(null);
		userStoryLabel.setOnMouseClicked(null);

		sendMessageBtn.setOnAction(null);
		messageContainer.getChildren().clear();

		Story currentStory = UserController.getChatController().currentStory();
		currentStory.getMessages().removeListener(messageListener);
	}
}