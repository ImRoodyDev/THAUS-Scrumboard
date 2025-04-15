package com.thaus.chatbox.components.informative;

import com.thaus.chatbox.classes.Message;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MessageComponent extends HBox implements ICustomNode {

	@FXML
	private Text content;
	@FXML
	private Label label;
	@FXML
	private Circle profile;

	// Private's
	private Message message;

	public MessageComponent(Message message) {
		this.message = message;
		initializeFXML("/components/informative/message.fxml");
	}

	@FXML
	public void initialize() {
		if (message.isMine()) {
			label.setText("You");
			this.getStyleClass().add("from-me");
		} else {
			String labelText = String.format("%s (%s)   %s", message.getSenderName(), message.getSenderId(), message.getTimestamp());
			label.setText(message.getSenderName());
		}

		// Set the message content
		content.setText(message.getContent());
	}
}
