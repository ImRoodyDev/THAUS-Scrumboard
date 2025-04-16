package com.thaus.chatbox.classes;

import javafx.collections.ObservableList;

public class UserStory {
	private String groupId;
	private String featureId;
	private String epicId;
	private String id;
	private String name;
	private String description;
	private ObservableList<Message> messages = javafx.collections.FXCollections.observableArrayList();

	private int unreadCount;

	public UserStory(String chatId, String featureId, String epicId, String id, String name, String description) {
		this.groupId = chatId;
		this.featureId = featureId;
		this.epicId = epicId;
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public UserStory(String featureId, String name) {
		this.featureId = featureId;
		this.name = name;
		this.description = "";
		this.id = "";
		this.groupId = "";
		this.epicId = "";
	}

	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getEpicId() {
		return epicId;
	}

	public void setEpicId(String epicId) {
		this.epicId = epicId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ObservableList<Message> getMessages() {
		return messages;
	}


	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}


	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public void removeMessage(Message message) {
		this.messages.remove(message);
	}


}
