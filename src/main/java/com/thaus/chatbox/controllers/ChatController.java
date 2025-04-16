package com.thaus.chatbox.controllers;

import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.User;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

// This class is responsible for managing the chat functionality and saving it in database
public class ChatController {
	// Selected chat
	private Group selectedGroup;
	// Action on chatboxSelectionAction
	private Runnable handleGroupSelection;
	// Action on chatbox deleted
	private Runnable handleGroupDeletion;
	// Set listChangeLister for the observable list
	private ListChangeListener<Group> chatListChangeListener;

	// On chatbox selected
	public void selectChatbox(Group group) {
		selectedGroup = group;

		if (handleGroupSelection != null) handleGroupSelection.run();
		System.out.println("Selected group: " + selectedGroup.getChatName());
	}

	// Create a new chatbox
	public void createGroup(ChatboxType type, String name) {
		System.out.println("New chat: " + name + " is group: " + type.getName());
		Group newChat = new Group(name, type);
		getCurrentUser().getGroups().addFirst(newChat);
	}

	// Delete group
	public void deleteGroup(Group chat) {
		getCurrentUser().getGroups().remove(chat);

		if (this.handleGroupDeletion != null) {
			handleGroupDeletion.run();
		}
	}

	// Get selected chat
	public Group getCurrentGroup() {
		return selectedGroup;
	}

	// Get all Groups
	public ObservableList<Group> getGroups() {
		return getCurrentUser().getGroups();
	}

	public void cleanup() {
		if (chatListChangeListener != null) {
			getCurrentUser().getGroups().removeListener(chatListChangeListener);
		}
		if (handleGroupSelection != null) {
			handleGroupSelection = null;
		}
		if (handleGroupDeletion != null) {
			handleGroupDeletion = null;
		}
	}

	// Chat controller action events
	public void handleGroupSelection(Runnable action) {
		handleGroupSelection = action;
	}

	// Chat controller on deleted event
	public void handleGroupDeletion(Runnable action) {
		handleGroupDeletion = action;
	}

	// Set chatbox list change listener
	public void setGroupListListener(ListChangeListener<Group> listener) {
		if (chatListChangeListener != null) {
			getCurrentUser().getGroups().removeListener(chatListChangeListener);
		}
		this.chatListChangeListener = listener;
		getCurrentUser().getGroups().addListener(listener);
	}

	public void removeChatboxesListener() {
		getCurrentUser().getGroups().removeListener(chatListChangeListener);
		chatListChangeListener = null;
		handleGroupSelection = null;
		handleGroupDeletion = null;
	}

	public void cleanGroupsListener() {
		if (chatListChangeListener != null) {
			getCurrentUser().getGroups().removeListener(chatListChangeListener);
		}
		if (handleGroupSelection != null) {
			handleGroupSelection = null;
		}
		if (handleGroupDeletion != null) {
			handleGroupDeletion = null;
		}
	}


	private User getCurrentUser() {
		return UserController.getCurrentUser();
	}
}

