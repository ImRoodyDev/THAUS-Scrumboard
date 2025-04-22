package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.classes.Story;
import com.thaus.chatbox.components.interactive.buttons.StoryButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class GroupStories extends VBox implements ICustomNode {
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

	private String description = " ";

	// Privates
	private String groupId;
	private String featureId;
	private String epicId;
	private Runnable onFeatureClickHandler;
	private Runnable onEpicClickHandler;
	private OnStoryClickHandler onStoryClickHandler;
	private ListChangeListener<Story> StoryListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Story story : change.getAddedSubList()) {
					createStoryComponent(story);
				}
			}

			if (change.wasRemoved()) {
				for (Story story : change.getRemoved()) {
					// Remove the feature from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof StoryButton storyButton) {
							return storyButton.getStoryId().equals(story.getId());
						}
						return false;
					});
				}
			}
		}
	};


	// Constructor
	public GroupStories() {
		initializeFXML("/components/tabs/group-stories.fxml");
	}

	@FXML
	public void initialize() {
		Feature storyFeature = UserController.getChatController().currentFeature();
		Epic storyEpic = UserController.getChatController().currentEpic();
		this.groupId = UserController.getChatController().currentGroup().getId();
		this.featureId = storyFeature.getId();
		this.epicId = storyEpic.getId();

		initializeLabels(storyFeature, storyEpic);
		initializeStories(storyFeature, storyEpic);
	}

	private void initializeLabels(Feature storyFeature, Epic storyEpic) {
		int featureIndex = UserController.getChatController().currentGroup().getFeatures().indexOf(storyFeature);
		int epicIndex = storyFeature.getEpics().indexOf(storyEpic);

		// Set the feature label
		this.featureLabel.setText("Feature " + (featureIndex + 1));
		this.epicLabel.setText("Epic " + (epicIndex + 1));
	}

	private void initializeStories(Feature storyFeature, Epic storyEpic) {
		// Check if user stories is null or empty
		if (storyEpic.getStories() == null || storyEpic.getStories().isEmpty()) {
			dialog.setVisible(true);
		} else {
			viewContainer.getChildren().remove(dialog);
		}

		// Add event listener to the array
		storyEpic.getStories().addListener(StoryListChangeListener);

		// Clear the view container and add each user story to it
		viewContainer.getChildren().clear();

		for (Story story : storyEpic.getStories()) {
			createStoryComponent(story);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createStory());
	}

	private void createStory() {
		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}

		JSONObject response = UserController.createStory(
				groupId,
				featureId,
				epicId,
				textField.getText(),
				description
		);

		if (response == null || response.getInt("statusCode") > 203) {
			return;
		} else {
			JSONObject story = response.getJSONObject("story");
			String storyId = story.getString("id");
			Epic storyEpic = UserController.getChatController().currentEpic();

			// Add the user story to the epic
			storyEpic.getStories().add(new Story(
					storyId,
					textField.getText(),
					description,
					null,
					null
			));

			// Clear the text field
			textField.clear();
		}
	}

	public void deleteStory(Story story) {
		// Check if the user story is null
		if (story == null) {
			return;
		}

		JSONObject response = UserController.deleteStory(
				groupId,
				featureId,
				epicId,
				story.getId()
		);

		if (response == null || response.getInt("statusCode") > 203) {
			System.out.println("Failed to delete story" + response.getString("message"));
			return;
		} else {
			Epic storyEpic = UserController.getChatController().currentEpic();
			storyEpic.getStories().remove(story);
			// Remove it to in the added sprint
			Sprint storySprint = UserController.getChatController().currentGroup().getSprintById(story.getSprintId().get());
			if (storySprint != null) {
				storySprint.removeUserStory(story);
			}
		}
	}

	public void addToSprint(Story story, Sprint sprint) {
		// Check if the sprint ID is null
		if (sprint == null) {
			return;
		}

		// Get the selected story
		String storyId = story.getId();
		String groupId = UserController.getChatController().currentGroup().getId();
		JSONObject response = UserController.linkStoryToSprint(
				groupId,
				sprint.getId(),
				storyId);

		if (response == null || response.getInt("statusCode") > 203) {
			System.out.println("Failed to link story to sprint");
			return;
		} else {
			System.out.println("Story linked to sprint");
			story.setSprintId(sprint.getId());
			sprint.addUserStory(story);
		}
	}

	private void createStoryComponent(Story story) {
		viewContainer.getChildren().remove(dialog);
		StoryButton storyButton = new StoryButton(story);

		// Set button handlers
		storyButton.handleOnClick(() -> {
			if (onStoryClickHandler != null) {
				onStoryClickHandler.onUserStoryClick(story);
			}
		});
		storyButton.handleOnDelete(() -> deleteStory(story));
		storyButton.handleOnSprintClick((sprint) -> addToSprint(story, sprint));
		viewContainer.getChildren().add(storyButton);
	}

	public void handleStoryClick(OnStoryClickHandler onStoryClickHandler) {
		this.onStoryClickHandler = onStoryClickHandler;
	}

	public void handleFeatureClick(Runnable onFeatureClickHandler) {
		this.onFeatureClickHandler = onFeatureClickHandler;
		featureLabel.setOnMouseClicked(_ -> {
			if (onFeatureClickHandler != null) {
				onFeatureClickHandler.run();
			}
		});
	}

	public void handleEpicClick(Runnable onEpicClickHandler) {
		this.onEpicClickHandler = onEpicClickHandler;
		epicLabel.setOnMouseClicked(_ -> {
			if (onEpicClickHandler != null) {
				onEpicClickHandler.run();
			}
		});
	}

	public void cleanup() {
		// Remove the listener from the user stories list
		viewContainer.getChildren().clear();
		onStoryClickHandler = null;
		onFeatureClickHandler = null;
		onEpicClickHandler = null;
		createButton.setOnAction(null);
		featureLabel.setOnMouseClicked(null);
		epicLabel.setOnMouseClicked(null);
		Epic storyEpic = UserController.getChatController().currentEpic();
		if (storyEpic.getStories() != null) {
			storyEpic.getStories().removeListener(StoryListChangeListener);
		}
	}

	public interface OnStoryClickHandler {
		void onUserStoryClick(Story story);
	}
}
