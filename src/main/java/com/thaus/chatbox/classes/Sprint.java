package com.thaus.chatbox.classes;

import com.thaus.chatbox.utils.DateUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class Sprint {
	private String id;
	private Date createdAt;

	// Observable properties
	private boolean messageInitialized = false;
	private StringProperty name = new javafx.beans.property.SimpleStringProperty();
	private StringProperty startedAt = new javafx.beans.property.SimpleStringProperty();
	private StringProperty endAt = new javafx.beans.property.SimpleStringProperty();
	private ObservableList<Story> userStories = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();

	public Sprint(String name, String id, Date startedAt, Date endAt) {
		this.id = id;
		this.name.set(name);
		if (startedAt != null)
			this.startedAt.set(DateUtils.formatDate(startedAt));
		if (endAt != null)
			this.endAt.set(DateUtils.formatDate(endAt));
		this.createdAt = new Date();
	}

	public Sprint(String name, String id, Date startedAt, Date endAt, Date createdAt) {
		this.id = id;
		this.name.set(name);
		if (startedAt != null)
			this.startedAt.set(DateUtils.formatDate(startedAt));
		if (endAt != null)
			this.endAt.set(DateUtils.formatDate(endAt));
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public StringProperty getName() {
		return name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public StringProperty getStartedAt() {
		return startedAt;
	}

	public StringProperty getEndAt() {
		return endAt;
	}

	public ObservableList<Story> getStories() {
		return userStories;
	}

	public ObservableList<Message> getMessages() {
		return messages;
	}

	public boolean isMessageInitialized() {
		return messageInitialized;
	}

	public void setMessageInitialized(boolean messageInitialized) {
		this.messageInitialized = messageInitialized;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt.set(DateUtils.formatDate(startedAt));
	}

	public void setEndAt(Date endAt) {
		this.endAt.set(DateUtils.formatDate(endAt));
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public void addMessages(ArrayList<Message> messages) {
		this.messages.addAll(0, messages);
	}

	public void addUserStory(Story story) {
		this.userStories.add(story);
	}

	public void removeUserStory(Story story) {
		this.userStories.remove(story);
	}

	public void updateStoryStatus(Story story, String userId, boolean isEnded) {
		// Handle starting a story sprint
		if (isEnded) {
			story.setUserId(userId);
			story.updateEndAt(new Date());

			// When all stories are ended set sprint endedAt
			if (userStories.stream().allMatch(s -> s.getUserId() != null)) {
				this.endAt.set(DateUtils.formatDate(new Date()));
			}
		} else {
			story.setUserId(userId);
			story.updateStartedAt(new Date());
		}
	}

	public void unlinkStories() {
		for (Story story : userStories) {
			story.setSprintId(null);
		}
		userStories.clear();
	}
}
