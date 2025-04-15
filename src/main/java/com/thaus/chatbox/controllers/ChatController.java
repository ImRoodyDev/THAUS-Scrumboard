package com.thaus.chatbox.controllers;

import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ChatController {
	// Change to ObservableList
	private final ObservableList<Group> chats = FXCollections.observableArrayList();

	// Selected chat
	private Group selectedChat;
	// Action on chatboxSelectionAction
	private Runnable onChatSelectionAction;
	// Action on chatbox deleted
	private Runnable onChatDeletedAction;
	// Set listChangeLister for the observable list
	private ListChangeListener<Group> chatListChangeListener;

	public ChatController() {

	}

	// Initialize chat controller listener
	public void initialize(ListChangeListener<Group> listener) {
		this.chatListChangeListener = listener;
		chats.addListener((ListChangeListener<Group>) listener);
	}

	public void setChatListListener(ListChangeListener<Group> listener) {
		if (chatListChangeListener != null) {
			chats.removeListener(chatListChangeListener);
		}
		this.chatListChangeListener = listener;
		chats.addListener(listener);
	}

	// Get chatbox list
	public ObservableList<Group> getChats() {
		return chats;
	}

	public Group getCurrentChat() {
		return selectedChat;
	}

	// Create a new chatbox
	public void createNewChatbox(ChatboxType type, String name) {
		System.out.println("New chat: " + name + " is group: " + type.getName());
		Group newChat = new Group(name, type);
		chats.addFirst(newChat);
	}

	// On chatbox selected
	public void selectChatbox(Group chat) {
		selectedChat = chat;

		if (onChatSelectionAction != null) onChatSelectionAction.run();
		System.out.println("Selected chat: " + selectedChat.getChatName());
	}

	// Delete chatbox
	public void deleteChatbox(Group chat) {
		chats.remove(chat);

		if (this.onChatDeletedAction != null) {
			onChatDeletedAction.run();
		}
	}
	
	// Chat controller action events
	public void setOnClickChatboxAction(Runnable action) {
		onChatSelectionAction = action;
	}

	// Chat controller on deleted event
	public void setOnClickChatboxDeletedAction(Runnable action) {
		onChatDeletedAction = action;
	}

	public void removeChatboxesListener() {
		chats.removeListener(chatListChangeListener);
		chatListChangeListener = null;
		onChatSelectionAction = null;
		onChatDeletedAction = null;
	}

	public void cleanup() {
		if (chatListChangeListener != null) {
			chats.removeListener(chatListChangeListener);
		}
		if (onChatSelectionAction != null) {
			onChatSelectionAction = null;
		}
		if (onChatDeletedAction != null) {
			onChatDeletedAction = null;
		}
	}

	public void cleanChatsListeners() {
		if (chatListChangeListener != null) {
			chats.removeListener(chatListChangeListener);
		}
		if (onChatSelectionAction != null) {
			onChatSelectionAction = null;
		}
		if (onChatDeletedAction != null) {
			onChatDeletedAction = null;
		}
	}
}

