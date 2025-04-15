package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.components.interactive.buttons.SprintButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GroupSprints extends VBox implements ICustomNode {
	private final Group group;

	@FXML
	private JFXButton createButton;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;
	@FXML
	private VBox viewContainer;
	private OnSprintClickHandler onSprintClickHandler;
	// private Runnable onDeleteHandler;
	private final ListChangeListener<Sprint> sprintListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Sprint sprint : change.getAddedSubList()) {
					createSprintComponent(sprint);
				}
			}

			if (change.wasRemoved()) {
				for (Sprint sprint : change.getRemoved()) {
					// Remove the feature from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof SprintButton sprintButton) {
							return sprintButton.getSprint().equals(sprint);
						}
						return false;
					});
				}
			}
		}
	};

	// Constructor
	public GroupSprints(Group group) {
		this.group = group;
		initializeFXML("/components/tabs/group-sprints.fxml");
	}

	@FXML
	public void initialize() {
		// Initialize labels
		initializeLabels();
		// Initialize the chat epics component
		initializeEpics();
	}

	// Initialize labels
	private void initializeLabels() {

	}

	// Initialize the Epics list
	private void initializeEpics() {
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
		createButton.setOnAction(_ -> createNewSprint());
	}

	private void createNewSprint() {
		// Create a new sprint
		group.createSprint();
	}

	private void createSprintComponent(Sprint sprint) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new sprint component and add it to the view container
		SprintButton sprintButton = new SprintButton(sprint);
		sprintButton.setOnClickHandler(() -> {
			if (onSprintClickHandler != null) {
				onSprintClickHandler.handle(sprint);
			}
		});
		sprintButton.setOnDeleteHandler(() -> group.deleteSprint(sprint));
		viewContainer.getChildren().add(sprintButton);
	}

	public void cleanup() {
		// Remove the listener from the features list
		group.getSprints().removeListener(sprintListChangeListener);
	}

	public void setOnSprintClickHandler(OnSprintClickHandler onEpicClickHandler) {
		this.onSprintClickHandler = onEpicClickHandler;
	}


	// Interface for the sprint click handler
	public interface OnSprintClickHandler {
		void handle(Sprint sprint);
	}
}
