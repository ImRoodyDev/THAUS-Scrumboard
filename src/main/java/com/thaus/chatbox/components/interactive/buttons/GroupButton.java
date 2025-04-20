package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.utils.ColorUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// ✅✅
public class GroupButton extends HBox implements ICustomNode {
	private boolean isMenuOpen = false;

	// Events
	private OnClickHandle onClickHandle;
	private OnDeleteHandle onDeleteHandle;

	@FXML
	private Label nameLabel;
	@FXML
	private Label profileLabel;
	@FXML
	private VBox profileBackground;
	@FXML
	private Label unreadLabel;
	@FXML
	private JFXButton chatboxBtn;
	@FXML
	private JFXButton contextMenuButton;
	@FXML
	private ContextMenu chatboxContextMenu;
	@FXML
	private MenuItem deleteMenuItem;
	@FXML
	private MenuItem clearMenuItem;

	// Group id
	private String groupId;

	// Constructor
	public GroupButton(Group group) {
		this.groupId = group.getId();
		initializeFXML("/components/interactive/group-button.fxml");
		initializeLabels(group);
	}

	@FXML
	public void initialize() {
		// Hook up actions
		if (chatboxContextMenu != null) {
			deleteMenuItem = new MenuItem("Delete");
			clearMenuItem = new MenuItem("Clear");

			// Create menu action
			chatboxContextMenu.getItems().addAll(deleteMenuItem, clearMenuItem);

			// Show context menu on button click
			contextMenuButton.setOnMouseClicked(e -> {
				if (!chatboxContextMenu.isShowing()) {
					chatboxContextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				} else {
					chatboxContextMenu.hide();
				}
			});
		}
	}

	public void initializeLabels(Group group) {
		this.getStyleClass().add(group.getType().getName().toLowerCase());
		if (nameLabel != null) {
			nameLabel.textProperty().bind(group.getName());
		}
		profileLabel.textProperty().bind(
				Bindings.createStringBinding(
						() -> {
							String username = group.getName().getValue();
							return (username != null && !username.isEmpty()) ?
									username.substring(0, 1).toUpperCase() : "";
						},
						group.getName()
				)
		);
		// Random color for the profile background skip black
		profileBackground.setStyle(
				profileBackground.getStyle() + " -fx-background-color: " + ColorUtils.getRandomColorExceptBlack()
		);

		// Set unread label
		unreadLabel.setVisible(false);
	}

	public String getGroupId() {
		return groupId;
	}

	// On click chatbox action
	public void onClickHandle(OnClickHandle handle) {
		this.onClickHandle = handle;
		if (chatboxBtn != null && onClickHandle != null) {
			chatboxBtn.setOnAction(_ -> onClickHandle.handle());
		}
	}

	// Set on delete action
	public void onDeleteAction(OnDeleteHandle onDelete) {
		this.onDeleteHandle = onDelete;
		if (deleteMenuItem != null && onDelete != null) {
			deleteMenuItem.setOnAction(_ -> onDelete.handle());
		}
	}


	public interface OnClickHandle {
		public void handle();
	}

	public interface OnDeleteHandle {
		public void handle();
	}
}
