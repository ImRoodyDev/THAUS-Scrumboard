package com.thaus.chatbox.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class Sprint {
	private String chatId = "";
	private String name = "FEFW";
	private String id;
	private int unreadCount;
	private Date startedAt;
	private Date endAt;
	private int userStoryCount;
	private ObservableList<UserStory> userStories = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();

	public Sprint() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public ObservableList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ObservableList<Message> messages) {
		this.messages = messages;
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public ObservableList<UserStory> getUserStories() {
		return userStories;
	}

	public void setUserStories(ObservableList<UserStory> userStories) {
		this.userStories = userStories;
	}

	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}

	public String getName() {
		return name;
	}

}
