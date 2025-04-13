package com.thaus.chatbox.components;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.ChatboxType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class ChatboxButton extends JFXButton implements ICustomNode {
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
	private JFXButton contextMenuButton;

	// Constructor
	public ChatboxButton(ChatboxType type, String id, String name, boolean isOwner, int unread) {
		initializeFXML();

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

	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/chatbox.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
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
		this.setOnAction(_ -> onClickHandle.handle(id));
	}

	// Toggle contextmenu
	private void toggleContextMenu() {
		isMenuOpen = !isMenuOpen;

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
