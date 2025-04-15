package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.util.Date;

public class SprintButton extends HBox implements ICustomNode {
	// private final Group group;
	private final Sprint sprint;

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
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	public SprintButton(Sprint sprint) {
		this.sprint = sprint;
		initializeFXML("/components/interactive/sprint.fxml");
	}

	public void initialize() {
		initializeMenu();
		initializeLabels();

		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});
	}

	private void initializeLabels() {
		// nameLabel.setText("Sprint " + (group.getSprints().indexOf(sprint) + 1));
		nameLabel.setText(sprint.getName());
		userStoryLabel.setText("User stories: " + String.valueOf(sprint.getUserStories().size()));
		unreadLabel.setText(String.valueOf(sprint.getUnreadCount()));

		if (sprint.getStartedAt() != null && sprint.getEndAt() == null) {
			statusLabel.setText("In progress");
			statusLabel.getStyleClass().add("yellow");
			dateLabel.setText("Started at: " + sprint.getStartedAt().toString());
		} else if (sprint.getEndAt() != null) {
			statusLabel.setText("Completed");
			statusLabel.getStyleClass().add("green");
			dateLabel.setText("Completed at: " + sprint.getEndAt().toString());

		} else {
			statusLabel.setText("Not started");
			statusLabel.getStyleClass().add("red");
			dateLabel.setText("Not started");
		}
	}

	private void initializeMenu() {
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
				startItem.setOnAction(e -> {
					// Handle start sprint action
					sprint.setStartedAt(new Date());
				});
			}

			if (sprint.getStartedAt() != null && sprint.getEndAt() == null) {
				MenuItem endItem = new MenuItem("End sprint");
				contextMenu.getItems().add(endItem);
				endItem.setOnAction(e -> {
					// Handle end sprint action
					sprint.setEndAt(new Date());
				});
			}

			// Open item action
			contextMenuButton.setOnAction(event -> {
				if (contextMenu != null) {
					contextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				}
			});
		}
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setOnClickHandler(Runnable handler) {
		this.onClickHandler = handler;
	}

	public void setOnDeleteHandler(Runnable handler) {
		this.onDeleteHandler = handler;
	}


}
