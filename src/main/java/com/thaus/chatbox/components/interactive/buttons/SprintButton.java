package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class SprintButton extends HBox implements ICustomNode {
	// FXML components
	@FXML
	private Label nameLabel;
	@FXML
	private Label userStoryLabel;
	@FXML
	private Label unreadLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private JFXButton button;
	@FXML
	private JFXButton contextMenuButton;
	@FXML
	private ContextMenu contextMenu;


	// Observable properties
	private final String sprintId;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;
	private OnSprintStartHandler onSprintStartHandler;
	private OnSprintStopHandler onSprintStopHandler;

	public SprintButton(Sprint sprint) {
		this.sprintId = sprint.getId();
		initializeFXML("/components/interactive/sprint.fxml");
	}

	public void initialize() {
		Sprint sprint = UserController.getChatController()
				.currentGroup().getSprintById(sprintId);

		initializeMenu(sprint);
		initializeLabels(sprint);

		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});
	}

	private void initializeLabels(Sprint sprint) {
		nameLabel.textProperty().bind(sprint.getName());
		userStoryLabel.setText("User stories: " + String.valueOf(sprint.getUserStories().size()));

		if (sprint.getStartedAt() != null && sprint.getEndAt() == null) {
			statusLabel.setText("In progress");
			statusLabel.getStyleClass().add("yellow");
			dateLabel.setText("Started at: " + sprint.getStartedAt().get());
		} else if (sprint.getEndAt() != null) {
			statusLabel.setText("Completed");
			statusLabel.getStyleClass().add("green");
			dateLabel.setText("Completed at: " + sprint.getEndAt().get());

		} else {
			statusLabel.setText("Not started");
			statusLabel.getStyleClass().add("red");
			dateLabel.setText("Not started");
		}
	}

	private void initializeMenu(Sprint sprint) {
		// Initialize the context menu
		if (contextMenuButton != null) {
			MenuItem deleteItem = new MenuItem("Delete");
			contextMenu.getItems().add(deleteItem);
			deleteItem.setOnAction(e -> {
				if (onDeleteHandler != null) {
					onDeleteHandler.run();
				}
			});

			if (sprint.getStartedAt() == null) {
				MenuItem startItem = new MenuItem("Start sprint");
				contextMenu.getItems().add(startItem);
				startItem.setOnAction(e -> onSprintStartHandler.handle(sprint));
			}

			if (sprint.getStartedAt() != null && sprint.getEndAt() == null) {
				MenuItem endItem = new MenuItem("End sprint");
				contextMenu.getItems().add(endItem);
				endItem.setOnAction(e -> onSprintStopHandler.handle(sprint));
			}

			// Open item action
			contextMenuButton.setOnAction(event -> {
				if (contextMenu != null) {
					contextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				}
			});
		}
	}

	public String getSprintId() {
		return sprintId;
	}

	public void handleSprintClick(Runnable handler) {
		this.onClickHandler = handler;
	}

	public void handleSprintDelete(Runnable handler) {
		this.onDeleteHandler = handler;
	}

	public void handleSprintStart(OnSprintStartHandler handler) {
		this.onSprintStartHandler = handler;
	}

	public interface OnSprintStartHandler {
		void handle(Sprint sprint);
	}

	public interface OnSprintStopHandler {
		void handle(Sprint sprint);
	}
	
}
