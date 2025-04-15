package com.thaus.chatbox.classes;

import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Group {
	private String chatId;
	private String chatName;
	private String createdAt;
	private int unreadCount;
	private boolean isOwner;
	private ChatboxType type;
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	private ObservableList<Feature> features = FXCollections.observableArrayList();
	private ObservableList<Sprint> sprints = FXCollections.observableArrayList();

	public Group(String chatName, ChatboxType type) {
		this.chatName = chatName;
		this.type = type;

		/*features.addListener((ListChangeListener<Feature>) change -> {
					while (change.next()) {
						if (change.wasAdded()) {
							System.out.println("Items added: " + change.getAddedSubList());
						}
						if (change.wasRemoved()) {
							System.out.println("Items removed: " + change.getRemoved());
						}
						if (change.wasUpdated()) {
							System.out.println("Items updated.");
						}
						if (change.wasPermutated()) {
							System.out.println("Items permutated.");
						}
					}
				}
		);*/
	}

	public Group(String chatId, String chatName, boolean isOwner, String createdAt) {
		this.chatId = chatId;
		this.chatName = chatName;
		this.isOwner = isOwner;
		this.createdAt = createdAt;
		this.messages = FXCollections.observableArrayList();
	}

	public void initializeChat(ArrayList<Message> messages) {
		this.messages = FXCollections.observableArrayList(messages);
	}

	public void initializeFeatures(ArrayList<Feature> features) {
		this.features = FXCollections.observableArrayList(features);
	}

	public void createSprint() {
		Sprint sprint = new Sprint();
		sprints.add(sprint);
	}

	public ObservableList<Sprint> getSprints() {
		return sprints;
	}

	public ObservableList<Message> getMessages() {
		return messages;
	}

	public ObservableList<Feature> getFeatures() {
		return features;
	}

	public void createFeature(String featureName) {
		Feature feature = new Feature(this.chatId, featureName);
		features.add(feature);
	}

	public void removeFeature(Feature feature) {
		features.remove(feature);
	}

	public void editFeature(Feature feature) {
		int index = features.indexOf(feature);
		if (index != -1) {
			features.set(index, feature);
		}
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void removeMessage(Message message) {
		messages.remove(message);
	}

	public void editMessage(Message message) {
		int index = messages.indexOf(message);
		if (index != -1) {
			messages.set(index, message);
		}
	}

	public ChatboxType getType() {
		return type;
	}

	public String getChatId() {
		return chatId;
	}

	public String getChatName() {
		return chatName;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public void deleteSprint(Sprint sprint) {
		sprints.remove(sprint);
	}
}
