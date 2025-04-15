package com.thaus.chatbox.components.tabs;

import com.thaus.chatbox.classes.ChatFeature;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ChatUserStories extends VBox implements ICustomNode {
	private final ChatFeature currentChatFeature;

	// Constructor
	public ChatUserStories(ChatFeature chatFeature) {
		this.currentChatFeature = chatFeature;
		initializeFXML("/components/tabs/chat-stories.fxml");
	}

	@FXML
	public void initialize() {
	}

	public void cleanup() {
	}
}
