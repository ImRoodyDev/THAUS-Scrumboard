package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Story;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class StoryButton extends HBox implements ICustomNode {

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
	private Story story;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	public StoryButton(Story story) {
		this.story = story;
		initializeFXML("/components/interactive/user-story.fxml");
	}

	public void initialize() {
		// Initialize the button with the name and id
		nameLabel.textProperty().bind(story.getName());

		// Set up the button actions
		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});

		initializeMenu();
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
					contextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				}
			});
		}
	}

	public Story getStory() {
		return story;
	}

	public void handleOnDelete(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void handleOnClick(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}
}
