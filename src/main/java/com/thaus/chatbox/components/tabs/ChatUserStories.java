package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.UserStory;
import com.thaus.chatbox.components.interactive.UserStoryButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatUserStories extends VBox implements ICustomNode {
	private final Epic currentEpicUserStory;
	@FXML
	private Label nameLabel;
	@FXML
	private Label userStoryLabel;
	@FXML
	private TextField textField;
	@FXML
	private JFXButton createButton;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;
	@FXML
	private VBox viewContainer;
	private OnUserStoryClickHandler onUserStoryClickHandler;
	// Privates
	private final ListChangeListener<UserStory> chatUserStoryListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (UserStory userStory : change.getAddedSubList()) {
					createUserStoryComponent(userStory);
				}
			}
		}
	};

	// Constructor
	public ChatUserStories(Epic epic) {
		this.currentEpicUserStory = epic;
		initializeFXML("/components/tabs/chat-stories.fxml");
	}

	@FXML
	public void initialize() {
		initializeLabels();
		initializeUserStory();
	}

	private void initializeLabels() {
		this.nameLabel.setText(currentEpicUserStory.getName());
		this.userStoryLabel.setText(String.valueOf(currentEpicUserStory.getUserStoryCount()));

	}

	private void initializeUserStory() {
		// Check if user stories is null or empty
		if (currentEpicUserStory.getUserStories() == null || currentEpicUserStory.getUserStories().isEmpty()) {
			return;
		}

		// Add event listener to the array
		currentEpicUserStory.getUserStories().addListener(chatUserStoryListChangeListener);

		// Clear the view container and add each user story to it
		viewContainer.getChildren().clear();

		for (UserStory userStory : currentEpicUserStory.getUserStories()) {
			createUserStoryComponent(userStory);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createUserStory());
	}

	private void createUserStory() {
		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}

		currentEpicUserStory.createUserStory(textField.getText());

		// Clear the text field
		textField.clear();
	}

	public void setOnUserStoryClickHandler(OnUserStoryClickHandler onUserStoryClickHandler) {
		this.onUserStoryClickHandler = onUserStoryClickHandler;
	}
	
	public void cleanup() {
	}

	private void createUserStoryComponent(UserStory userStory) {
		UserStoryButton userStoryButton = new UserStoryButton(userStory);
		userStoryButton.setOnClickHandler(() -> {
			if (onUserStoryClickHandler != null) {
				onUserStoryClickHandler.onUserStoryClick(userStory);
			}
		});
		viewContainer.getChildren().add(userStoryButton);
	}

	public interface OnUserStoryClickHandler {
		void onUserStoryClick(UserStory userStory);
	}

}
