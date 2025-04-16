package com.thaus.chatbox.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	private String username;
	private ObservableList<Group> groups = FXCollections.observableArrayList();
	private ObservableList<String> notifications = FXCollections.observableArrayList();

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void removeGroup(Group group) {
		groups.remove(group);
	}

	public ObservableList<Group> getGroups() {
		return groups;
	}

	public ObservableList<String> getNotifications() {
		return notifications;
	}

	public void cleanup() {

	}
}
