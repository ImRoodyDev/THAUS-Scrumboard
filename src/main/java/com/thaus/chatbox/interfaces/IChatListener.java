package com.thaus.chatbox.interfaces;

import com.thaus.chatbox.components.ChatboxButton;

import java.util.ArrayList;

public interface IChatListener {
	public void initializeChatboxs(ArrayList<ChatboxButton> chatboxButtons);

	public void newChatbox(ChatboxButton chatboxButton);

	public void deleteChatbox(ChatboxButton chatboxButton);

	public void onChatboxsUpdated(ArrayList<ChatboxButton> chatboxButtons);
}
