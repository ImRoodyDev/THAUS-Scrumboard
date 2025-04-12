package com.thaus.chatbox.controllers;

import com.thaus.chatbox.components.ChatboxButton;
import com.thaus.chatbox.interfaces.IChatListener;
import com.thaus.chatbox.types.ChatboxType;

import java.util.ArrayList;

public class ChatController {
	// Chatbox listener
	private IChatListener chatListener;
	// Selected Chatbox id
	private String selectedChatboxId;
	// Array of all the chatbox that the user can see
	private ArrayList<ChatboxButton> chatboxButtons = new ArrayList<>();


	public ChatController(IChatListener listener) {
		this.chatListener = listener;
	}

	public void loadChatbox() {

	}

	public void createNewChatbox(ChatboxType type, String name) {
		System.out.println("New chat: " + name + " is group: " + type.getName());
		/*ChatboxButton newChatboxButton = new ChatboxButton(ChatboxType.)

		chatListener.newChatbox();*/
	}
}

