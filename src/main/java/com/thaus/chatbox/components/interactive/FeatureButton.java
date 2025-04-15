package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class FeatureButton extends HBox implements ICustomNode {

	// Privates
	private final Feature chatFeature;
	@FXML
	private Label nameLabel;
	@FXML
	private Label epicsLabel;
	@FXML
	private Label userStoryLabel;
	@FXML
	private Label unreadLabel;
	@FXML
	private JFXButton button;
	@FXML
	private JFXButton contextMenuButton;
	@FXML
	private ContextMenu contextMenu;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	// Constructor
	public FeatureButton(Feature chatFeature) {
		this.chatFeature = chatFeature;

		// Initialize the button with the name and id
		initializeFXML("/components/interactive/feature.fxml");
		initializeMenu();
	}

	@FXML
	public void initialize() {
		// Initialize the button with the name and id
		this.epicsLabel.setText(String.valueOf(chatFeature.getEpicsCount()));
		this.userStoryLabel.setText(String.valueOf(chatFeature.getUserStoryCount()));
		this.unreadLabel.setText(String.valueOf(chatFeature.getUnreadCount()));
		this.nameLabel.setText(chatFeature.getName());

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

	public Feature getFeature() {
		return chatFeature;
	}

	public void setOnDeleteHandler(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void setOnClickHandler(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}

}
