package com.thaus.chatbox.controllers;

import com.thaus.chatbox.components.interactive.ChatboxButton;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Set;

public class ChatController {
	// Change to ObservableList
	private final ObservableList<ChatboxButton> allChatboxes = FXCollections.observableArrayList();

	// Selected chat
	private String selectedChatId;
	// Action on chatboxSelectionAction
	private Runnable onChatSelectionAction;
	// Action on chatbox deleted
	private Runnable onChatboxDeletedAction;
	// Set listChangeLister for the observable list
	private ListChangeListener<ChatboxButton> listener;

	// Initialize chat controller listener
	public void initialize(ListChangeListener<ChatboxButton> listener) {
		this.listener = listener;
		allChatboxes.addListener((ListChangeListener<ChatboxButton>) listener);
	}

	// Get controller get chatboxs button
	public ObservableList<ChatboxButton> getChatboxButtons() {
		return allChatboxes;
	}

	// Get selected chatbox id
	public String getSelectedChatId() {
		return this.selectedChatId;
	}

	// Create a new chatbox
	public void createNewChatbox(ChatboxType type, String name) {
		System.out.println("New chat: " + name + " is group: " + type.getName());
		ChatboxButton newChatboxButton = new ChatboxButton(type, "0", name, true, 10);
		allChatboxes.addFirst(newChatboxButton);
		newChatboxButton.onClickHandle(this::onChatboxSelected);
		newChatboxButton.onDeleteAction(() -> deleteChatbox(newChatboxButton));
	}

	// Delete chatbox
	public void deleteChatbox(ChatboxButton button) {
		allChatboxes.remove(button);

		if (this.onChatboxDeletedAction != null) {
			onChatboxDeletedAction.run();
		}
	}

	// Apply filter to search chatboxes
	public void filterChatboxes(Set<ChatboxType> filters) {
		ArrayList<ChatboxButton> filtered = (ArrayList<ChatboxButton>) allChatboxes.stream()
				.filter(b -> filters.contains(b.getType()))
				.toList();
	}

	// Search for chatboxes/messages which will return the chatboxes
	public void searchChatboxes(String query) {
		ArrayList<ChatboxButton> results = (ArrayList<ChatboxButton>) allChatboxes.stream()
				.filter(b -> b.getName().contains(query))
				.toList();
	}

	// On chatbox selected
	private void onChatboxSelected(String id) {
		selectedChatId = id;
		if (onChatSelectionAction != null) onChatSelectionAction.run();
		System.out.println("Selected chat: " + selectedChatId);
	}

	// Chat controller action events
	public void setOnClickChatboxAction(Runnable action) {
		onChatSelectionAction = action;
	}

	// Chat controller on deleted event
	public void setOnClickChatboxDeletedAction(Runnable action) {
		onChatboxDeletedAction = action;
	}

	public void removeChatboxesListener() {
		allChatboxes.removeListener(listener);
		listener = null;
		onChatSelectionAction = null;
		onChatboxDeletedAction = null;
	}

	public void sendGeneralMessage(String chatboxId, String text) {
	}
}

