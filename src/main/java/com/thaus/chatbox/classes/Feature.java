package com.thaus.chatbox.classes;

import javafx.collections.ObservableList;

public class Feature {
	private String groupId;
	private String id;
	private String name;
	private String description;
	private String status;
	private int unreadCount = 0;
	private int epicsCount = 0;
	private int userStoryCount = 0;
	private ObservableList<Epic> epics = javafx.collections.FXCollections.observableArrayList();

	public Feature(String id, String name, String description, String status, int unread, int epicsCount, int userStoryCount) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.unreadCount = unread;
		this.epicsCount = epicsCount;
		this.userStoryCount = userStoryCount;
	}

	public Feature(String groupId, String name) {
		this.name = name;
		this.groupId = groupId;
		this.description = "";
		this.status = "New";
		this.unreadCount = 0;
		this.epicsCount = 0;
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

	public int getEpicsCount() {
		return epicsCount;
	}

	public void setEpicsCount(int epicsCount) {
		this.epicsCount = epicsCount;
	}

	public int getUserStoryCount() {
		return userStoryCount;
	}

	public void setUserStoryCount(int userStoryCount) {
		this.userStoryCount = userStoryCount;
	}

	public ObservableList<Epic> getEpics() {
		return epics;
	}

	public void createEpic(String name) {
		Epic newEpic = new Epic(this.id, name);
		this.epics.add(newEpic);
		this.epicsCount++;
	}

	public void deleteEpic(Epic epic) {
		this.epics.remove(epic);
		this.epicsCount--;
	}
}
