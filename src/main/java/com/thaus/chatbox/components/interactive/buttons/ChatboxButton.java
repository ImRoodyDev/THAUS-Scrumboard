package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class ChatboxButton extends HBox implements ICustomNode {
	private final Group chat;

	// Menu is open
	private boolean isMenuOpen = false;

	// Events
	private OnDeleteHandle onDeleteHandle;
	private OnClickHandle onClickHandle;

	@FXML
	private Label nameLabel;
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

	// Constructor
	public ChatboxButton(Group chat) {
		this.chat = chat;
		initializeFXML("/components/tabs/group-box.fxml");
	}

	@FXML
	public void initialize() {
		initializeLabels();

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

	public void initializeLabels() {
		if (nameLabel != null) {
			nameLabel.setText(chat.getChatName());
		}
		if (unreadLabel != null) {
			unreadLabel.setText(String.valueOf(chat.getUnreadCount()));

			unreadLabel.setVisible(chat.getUnreadCount() > 0);
		}
		this.getStyleClass().add(chat.getType().getName().toLowerCase());
	}

	// On click chatbox action
	public void onClickHandle(OnClickHandle handle) {
		this.onClickHandle = handle;
		if (chatboxBtn != null && onClickHandle != null) {
			chatboxBtn.setOnAction(_ -> onClickHandle.handle(chat));
		}
	}

	// Set on delete action
	public void onDeleteAction(OnDeleteHandle onDelete) {
		this.onDeleteHandle = onDelete;
		if (deleteMenuItem != null && onDelete != null) {
			deleteMenuItem.setOnAction(_ -> onDelete.handle(chat));
		}
	}

	public Group getChat() {
		return chat;
	}

	public interface OnClickHandle {
		public void handle(Group chat);
	}

	public interface OnDeleteHandle {
		public void handle(Group chat);
	}
}
