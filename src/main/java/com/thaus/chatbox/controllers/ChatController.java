package com.thaus.chatbox.controllers;

import com.thaus.chatbox.interfaces.IChatListener;

public class ChatController {
	// Chatbox listener
	private IChatListener chatListener;
	// Selected Chatbox id
	private String selectedChatboxId;

	public ChatController(IChatListener listener) {
		this.chatListener = listener;
	}

	public void loadChatbox() {

	}
}

