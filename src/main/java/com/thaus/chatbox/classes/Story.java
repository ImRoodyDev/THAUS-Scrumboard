package com.thaus.chatbox.classes;

import com.thaus.chatbox.utils.DateUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class Story {
	private String id;

	// Observable properties
	private boolean messageInitialized = false;
	private StringProperty name = new javafx.beans.property.SimpleStringProperty();
	private StringProperty description = new javafx.beans.property.SimpleStringProperty();
	private StringProperty sprintId = new javafx.beans.property.SimpleStringProperty();
	private StringProperty userId = new javafx.beans.property.SimpleStringProperty();
	private StringProperty startedAt = new javafx.beans.property.SimpleStringProperty();
	private StringProperty endAt = new javafx.beans.property.SimpleStringProperty();
	private ObservableList<Message> messages = javafx.collections.FXCollections.observableArrayList();


	public Story(String id, String name, String description, String sprintId, String userId) {
		this.id = id;
		this.name.set(name);
		this.description.set(description);
		this.sprintId.set(sprintId);
		this.userId.set(userId);
	}

	public String getId() {
		return id;
	}

	public StringProperty getStartedAt() {
		return startedAt;
	}

	public StringProperty getEndAt() {
		return endAt;
	}

	public StringProperty getName() {
		return name;
	}

	public StringProperty getDescription() {
		return description;
	}

	public StringProperty getSprintId() {
		return sprintId;
	}

	public StringProperty getUserId() {
		return userId;
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

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public void addMessages(ArrayList<Message> messages) {
		this.messages.addAll(0, messages);
	}

	public void setSprintId(String sprintId) {
		this.sprintId.setValue(sprintId);
	}

	public void setUserId(String userId) {
		this.userId.setValue(userId);
	}

	public void updateDates(Date startedAt, Date endAt) {
		this.startedAt.set(DateUtils.formatDate(startedAt));
		this.endAt.set(DateUtils.formatDate(endAt));
	}

	public void updateStartedAt(Date startedAt) {
		this.startedAt.set(DateUtils.formatDate(startedAt));
	}

	public void updateEndAt(Date endAt) {
		this.endAt.set(DateUtils.formatDate(endAt));
	}

}
