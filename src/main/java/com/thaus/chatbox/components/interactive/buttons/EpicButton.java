package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class EpicButton extends HBox implements ICustomNode {
	// FXML components
	@FXML
	private Label nameLabel;
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
	private final Epic featureEpic;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	public EpicButton(Epic featureEpic) {
		this.featureEpic = featureEpic;
		initializeFXML("/components/interactive/epic.fxml");
		initializeMenu();
	}

	@FXML
	public void initialize() {
		// Initialize the button with the name and id
		this.nameLabel.setText(featureEpic.getName());
		this.userStoryLabel.textProperty().bind(Bindings.concat(
				"User stories: ", featureEpic.getStoryCount().asString()
		));

		// Set up the button actions
		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});
	}

	public Epic getEpic() {
		return featureEpic;
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

			// Open item action
			contextMenuButton.setOnAction(event -> {
				if (contextMenu != null) {
					contextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				}
			});
		}
	}

	public void handleDeleteClick(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void handleEpicClick(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}

}
