package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.UserStory;
import com.thaus.chatbox.components.interactive.buttons.UserStoryButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GroupUserStories extends VBox implements ICustomNode {
	private final Epic currentUserStoryEpic;
	private final Feature currentUserStoryFeature;

	@FXML
	private Label featureLabel;
	@FXML
	private Label epicLabel;
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

			if (change.wasRemoved()) {
				for (UserStory userStory : change.getRemoved()) {
					// Remove the feature from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof UserStoryButton userStoryButton) {
							return userStoryButton.getUserStory().equals(userStory);
						}
						return false;
					});
				}
			}
		}
	};

	// Constructor
	public GroupUserStories(Epic epic, Feature feature) {
		this.currentUserStoryEpic = epic;
		this.currentUserStoryFeature = feature;
		initializeFXML("/components/tabs/group-stories.fxml");
	}

	@FXML
	public void initialize() {
		initializeLabels();
		initializeUserStory();
	}

	private void initializeLabels() {
		// Set the feature label
		this.featureLabel.setText(currentUserStoryFeature.getName());
		this.epicLabel.setText(currentUserStoryEpic.getName());
	}

	private void initializeUserStory() {
		// Check if user stories is null or empty
		if (currentUserStoryEpic.getUserStories() == null || currentUserStoryEpic.getUserStories().isEmpty()) {
			dialog.setVisible(true);
		} else {
			viewContainer.getChildren().remove(dialog);
		}

		// Add event listener to the array
		currentUserStoryEpic.getUserStories().addListener(chatUserStoryListChangeListener);

		// Clear the view container and add each user story to it
		viewContainer.getChildren().clear();

		for (UserStory userStory : currentUserStoryEpic.getUserStories()) {
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

		currentUserStoryEpic.createUserStory(textField.getText());

		// Clear the text field
		textField.clear();
	}

	public void setOnUserStoryClickHandler(OnUserStoryClickHandler onUserStoryClickHandler) {
		this.onUserStoryClickHandler = onUserStoryClickHandler;
	}

	public void cleanup() {
		// Remove the listener from the user stories list
		if (currentUserStoryEpic.getUserStories() != null) {
			currentUserStoryEpic.getUserStories().removeListener(chatUserStoryListChangeListener);
		}
	}

	private void createUserStoryComponent(UserStory userStory) {
		viewContainer.getChildren().remove(dialog);

		UserStoryButton userStoryButton = new UserStoryButton(userStory);

		userStoryButton.setOnClickHandler(() -> {
			if (onUserStoryClickHandler != null) {
				onUserStoryClickHandler.onUserStoryClick(userStory);
			}
		});

		userStoryButton.setOnDeleteHandler(() -> currentUserStoryEpic.deleteUserStory(userStory));
		viewContainer.getChildren().add(userStoryButton);
	}

	public interface OnUserStoryClickHandler {
		void onUserStoryClick(UserStory userStory);
	}
}
