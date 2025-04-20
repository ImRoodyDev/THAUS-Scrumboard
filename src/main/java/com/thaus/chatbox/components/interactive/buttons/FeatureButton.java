package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class FeatureButton extends HBox implements ICustomNode {
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

	// Observable properties
	private Feature feature;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	// Constructor
	public FeatureButton(Feature feature) {
		this.feature = feature;

		// Initialize the button with the name and id
		initializeFXML("/components/interactive/feature.fxml");
		initializeMenu();
	}

	@FXML
	public void initialize() {
		// Initialize the button with the name and id
		this.nameLabel.setText(feature.getName());
		this.userStoryLabel.textProperty().bind(Bindings.concat(
				"User stories: ", feature.getStoryCount().asString()
		));
		this.epicsLabel.textProperty().bind(Bindings.concat(
				"Epics: ", feature.getEpicCount().asString()
		));

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
			contextMenu.getItems().addAll(deleteItem);

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

	public Feature getFeature() {
		return feature;
	}

	public void handleOnDelete(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void handleOnClick(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}

}
