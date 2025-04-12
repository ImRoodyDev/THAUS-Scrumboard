package com.thaus.chatbox.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CreateChatMenuComponent extends VBox implements ICustomNode {
	private final int groupNameFieldIndex = 2;
	private final int userNameFieldIndex = 3;
	private final ObservableList<Node> children = this.getChildren();

	// Components
	@FXML
	private JFXComboBox<String> chatTypeCombo;
	@FXML
	private TextField groupNameField;
	@FXML
	private TextField userNameField;
	@FXML
	private JFXButton createBt;
	@FXML
	private JFXButton cancelBt;

	// Component controllers
	private ChatboxType chatType;
	private OnActionSubmitHandler onActionSubmit;
	private Runnable onCancelAction;

	public CreateChatMenuComponent() {
		initializeFXML();
	}

	@FXML
	public void initialize() {
		// Initialize combo box
		ChatboxType[] types = ChatboxType.values();

		if (chatTypeCombo != null) {
			for (ChatboxType type : types) {
				chatTypeCombo.getItems().add(type.getName());
			}

			// Set default selection to TEAM
			chatTypeCombo.getSelectionModel().select(ChatboxType.TEAM.getName());
			enableGroupField(ChatboxType.TEAM);

			chatTypeCombo.setOnAction(_ -> {
				String selectedItem = chatTypeCombo.getSelectionModel().getSelectedItem();
				ChatboxType type = ChatboxType.fromName(chatTypeCombo.getSelectionModel().getSelectedItem());
				enableGroupField(type);
			});
		}


	}

	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/create-chat.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
		}
	}

	// Set on submit
	public void setOnActionSubmit(OnActionSubmitHandler handler) {
		this.onActionSubmit = handler;
		createBt.setOnAction(_ -> {
					String name = ChatboxType.TEAM == chatType ? groupNameField.getText() : userNameField.getText();
					onActionSubmit.handle(chatType, name);
				}
		);
	}

	// Set on cancel
	public void setOnCancelAction(Runnable cancelHandler) {
		this.onCancelAction = cancelHandler;
		cancelBt.setOnAction(_ -> onCancelAction.run());
	}

	// Enable group form
	private void enableGroupField(ChatboxType type) {
		chatType = type;
		if (ChatboxType.TEAM == type) {
			// Show group name field, hide username field
			children.get(groupNameFieldIndex).setVisible(true);
			children.get(userNameFieldIndex).setVisible(false);

			children.get(groupNameFieldIndex).setManaged(true);
			children.get(userNameFieldIndex).setManaged(false);
		} else {
			// Show username field, hide group name field
			children.get(groupNameFieldIndex).setVisible(false);
			children.get(userNameFieldIndex).setVisible(true);

			children.get(groupNameFieldIndex).setManaged(false);
			children.get(userNameFieldIndex).setManaged(true);
		}
	}

	//  Create Chat handler
	public interface OnActionSubmitHandler {
		void handle(ChatboxType type, String name);
	}
}
