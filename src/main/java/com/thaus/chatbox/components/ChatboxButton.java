package com.thaus.chatbox.components;

import com.thaus.chatbox.types.ChatboxType;

public class ChatboxButton {
	private final String id;
	// Type of the chatbox
	private final ChatboxType type;
	// Name of the chatbox
	private final String name;
	// Is owner of the chatbox
	private final boolean isOwner;
	// Count of unread messages
	private int unread = 0;

	// Constructor
	public ChatboxButton(ChatboxType type,String id, String name, boolean isOwner, int unread) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.isOwner = isOwner;
		this.unread = unread;
	}



}
