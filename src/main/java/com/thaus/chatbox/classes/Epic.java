package com.thaus.chatbox.classes;

import javafx.collections.ObservableList;

public class Epic {
	private String chatId;
	private String featureId;
	private String id;
	private String name;
	private String description;
	private String status;
	private int unreadCount = 0;
	private int userStoryCount = 0;

	private ObservableList<UserStory> userStories = javafx.collections.FXCollections.observableArrayList();

	public Epic(String id, String name, String description, String status, int unread, int userStoryCount) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.unreadCount = unread;
		this.userStoryCount = userStoryCount;
	}

	public Epic(String featureId, String name) {
		this.name = name;
		this.featureId = featureId;
		this.description = "";
		this.status = "New";
		this.unreadCount = 0;
		this.userStoryCount = 0;
	 
	}

	public String getId() {
		return id;
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

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public int getUserStoryCount() {
		return userStoryCount;
	}

	public void createUserStory(String name) {
		UserStory userStory = new UserStory(this.featureId, name);
		userStories.add(userStory);
		userStoryCount++;
	}

	public void deleteUserStory(UserStory userStory) {
		userStories.remove(userStory);
		userStoryCount--;
	}

	public ObservableList<UserStory> getUserStories() {
		return userStories;
	}
}
