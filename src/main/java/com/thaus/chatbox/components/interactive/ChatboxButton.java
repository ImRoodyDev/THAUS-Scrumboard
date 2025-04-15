package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.ChatboxType;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class ChatboxButton extends HBox implements ICustomNode {
	private final String id;
	// Type of the chatbox
	private final ChatboxType type;
	// Is owner of the chatbox
	private final boolean isOwner;
	// Name of the chatbox
	private String name;
	// Count of unread messages
	private int unread = 0;
	// Menu is open
	private boolean isMenuOpen = false;

	// Events
	private Runnable onDelete;
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
	public ChatboxButton(ChatboxType type, String id, String name, boolean isOwner, int unread) {
		initializeFXML("/components/tabs/chatbox.fxml");

		this.type = type;
		this.id = id;
		this.name = name;
		this.isOwner = isOwner;
		this.unread = unread;

		// Add styling class of the type
		nameLabel.setText(name);
		unreadLabel.setText(String.valueOf(unread));
		this.getStyleClass().add(type.getName().toLowerCase());
	}

	@FXML
	public void initialize() {
		// Hook up actions
		if (chatboxContextMenu != null) {
			MenuItem deleteItem = new MenuItem("Delete");
			MenuItem clearItem = new MenuItem("Clear");

			// Create menu action
			deleteItem.setOnAction(e -> onDelete());
			clearItem.setOnAction(e -> updateUnread(0)); // Example clear action
			chatboxContextMenu.getItems().addAll(deleteItem, clearItem);

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

	// Events on new unread
	public void updateUnread(int number) {
		unreadLabel.setText(String.valueOf(number));
	}

	// Set on delete action
	public void onDeleteAction(Runnable onDelete) {
		this.onDelete = onDelete;
	}

	// Get type of the chatbox
	public ChatboxType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void onClickHandle(OnClickHandle handle) {
		onClickHandle = handle;
		if (chatboxBtn != null && onClickHandle != null) {
			chatboxBtn.setOnAction(_ -> onClickHandle.handle(id));
		}
	}


	private void onDelete() {
		if (onDelete != null) {
			onDelete.run();
		}
	}

	public interface OnClickHandle {

		public void handle(String id);

		// 	public void handle(String id, ChatboxType type);
	}
}
