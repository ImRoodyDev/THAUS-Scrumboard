package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.components.interactive.buttons.SprintButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class GroupSprints extends VBox implements ICustomNode {
	@FXML
	private JFXButton createButton;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;
	@FXML
	private VBox viewContainer;

	// Observable properties
	private String groupId;
	private OnSprintClickHandler onSprintClickHandler;
	private final ListChangeListener<Sprint> sprintListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Sprint sprint : change.getAddedSubList()) {
					createSprintComponent(sprint);
				}
			}

			if (change.wasRemoved()) {
				for (Sprint sprint : change.getRemoved()) {
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof SprintButton sprintButton) {
							return sprintButton.getSprintId().equals(sprint.getId());
						}
						return false;
					});
				}

				// Update sprint names after any change
				updateSprintNames();
			}
		}
	};

	// Constructor
	public GroupSprints() {
		initializeFXML("/components/tabs/group-sprints.fxml");
	}

	@FXML
	public void initialize() {
		Group group = UserController.getChatController().currentGroup();
		this.groupId = group.getId();

		// Initialize the chat epics component
		initializeEpics(group);
	}

	// Initialize the Epics list
	private void initializeEpics(Group group) {
		// Check if epics is null or empty
		if (group.getSprints() == null || group.getSprints().isEmpty()) {
			dialog.setVisible(true);
		} else {
			if (viewContainer.getChildren().contains(dialog)) {
				viewContainer.getChildren().remove(dialog);
			}
		}

		// Clear the view container and add each epic to it
		viewContainer.getChildren().clear();

		// Add the listener to the epics list
		group.getSprints().addListener(sprintListChangeListener);


		// Add each epic to the view container
		for (Sprint sprint : group.getSprints()) {
			createSprintComponent(sprint);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createSprint());
	}

	private void createSprint() {
		System.out.println("Create sprint");

		JSONObject response = UserController.addSprint(groupId);

		if (response.getInt("statusCode") > 203) {
			System.out.println("Failed to create sprint: " + response.getString("message"));
		} else {
			JSONObject sprint = response.getJSONObject("sprint");
			String sprintId = sprint.getString("id");
			Group currentGroup = UserController.getChatController().currentGroup();
			String sprintName = "Sprint " + (currentGroup.getSprints().size() + 1);
			currentGroup.addSprint(new Sprint(
					sprintName,
					sprintId,
					null,
					null
			));
		}
	}

	private void deleteSprint(Sprint sprint) {

		JSONObject response = UserController.deleteSprint(groupId, sprint.getId());

		if (response == null || response.getInt("statusCode") > 203) {
			System.out.println("Failed to delete sprint: " + response.getString("message"));
		} else {
			Group currentGroup = UserController.getChatController().currentGroup();
			currentGroup.deleteSprint(sprint); // Ensure this updates the observable list
		}
	}

	private void createSprintComponent(Sprint sprint) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new sprint component and add it to the view container
		SprintButton sprintButton = new SprintButton(sprint);
		sprintButton.handleSprintClick(() -> {
			if (onSprintClickHandler != null) {
				onSprintClickHandler.handle(sprint);
			}
		});
		sprintButton.handleSprintDelete(() -> deleteSprint(sprint));
		viewContainer.getChildren().add(sprintButton);
	}

	private void updateSprintNames() {
		int index = 1;
		Group group = UserController.getChatController().currentGroup();
		for (Sprint sprint : group.getSprints()) {
			sprint.setName("Sprint " + index); // Update the name property
			index++;
		}
	}

	public void handleSprintClick(OnSprintClickHandler onEpicClickHandler) {
		this.onSprintClickHandler = onEpicClickHandler;
	}

	public void cleanup() {
		// Remove the listener from the features list
		Group group = UserController.getChatController().currentGroup();
		group.getSprints().removeListener(sprintListChangeListener);
		onSprintClickHandler = null;
	}

	// Interface for the sprint click handler
	public interface OnSprintClickHandler {
		void handle(Sprint sprint);
	}
}
