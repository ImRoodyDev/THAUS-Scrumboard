package com.thaus.chatbox.classes;

public class ChatFeature {
	private String id;
	private String name;
	private String description;
	private String status;
	private int unread = 0;
	private int epics = 0;
	private int userStory = 0;

	public ChatFeature(String id, String name, String description, String status, int unread, int epics, int userStory) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.unread = unread;
		this.epics = epics;
		this.userStory = userStory;
	}

	public ChatFeature(String name) {
		this.name = name;
		this.id = "";
		this.description = "";
		this.status = "New";
		this.unread = 0;
		this.epics = 0;
		this.userStory = 0;
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

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public int getEpics() {
		return epics;
	}

	public void setEpics(int epics) {
		this.epics = epics;
	}

	public int getUserStory() {
		return userStory;
	}

	public void setUserStory(int userStory) {
		this.userStory = userStory;
	}
}
