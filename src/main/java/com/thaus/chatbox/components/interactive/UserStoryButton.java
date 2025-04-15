package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.UserStory;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class UserStoryButton extends HBox implements ICustomNode {

	@FXML
	private Label nameLabel;
	@FXML
	private Label unreadLabel;
	@FXML
	private JFXButton button;
	@FXML
	private JFXButton contextMenuButton;
	@FXML
	private ContextMenu contextMenu;

	//
	private UserStory userStory;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	public UserStoryButton(UserStory userStory) {
		this.userStory = userStory;
		initializeFXML("/components/interactive/user-story.fxml");
	}

	public void initialize() {
		// Initialize the button with the name and id
		nameLabel.setText(userStory.getName());

		// Set up the button actions
		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});

	}

	private void initializeMenu() {
		// Initialize the context menu
		if (contextMenuButton != null) {
			MenuItem deleteItem = new MenuItem("Delete");
			MenuItem openItem = new MenuItem("Open");
			contextMenu.getItems().addAll(deleteItem, openItem);

			// Create menu action
			deleteItem.setOnAction(e -> {
				if (onDeleteHandler != null) {
					onDeleteHandler.run();
				}
			});


			contextMenuButton.setOnAction(event -> {
				if (contextMenu != null) {
					contextMenu.show(contextMenuButton, 0, 0);
				}
			});
		}
	}

	public void setOnDeleteHandler(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void setOnClickHandler(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}
}
