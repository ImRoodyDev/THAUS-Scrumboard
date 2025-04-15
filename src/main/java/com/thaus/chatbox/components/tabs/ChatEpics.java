package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.ChatFeature;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatEpics extends VBox implements ICustomNode {
	private final ChatFeature currentChatFeature;

	@FXML
	private Label nameLabel;
	@FXML
	private Label epicsLabel;
	@FXML
	private Label userStoryLabel;
	@FXML
	private TextField textField;
	@FXML
	private JFXButton createButton;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;
	@FXML
	private VBox viewContainer;

	private OnEpicClickHandler onEpicClickHandler;

	// Constructor
	public ChatEpics(ChatFeature chatFeature) {
		this.currentChatFeature = chatFeature;
		initializeFXML("/components/tabs/chat-epics.fxml");
	}

	@FXML
	public void initialize() {
		// Initialize the chat epics component
	}

	public void cleanup() {

	}

	public interface OnEpicClickHandler {
		void onEpicClick(ChatFeature chatFeature);
	}
}
