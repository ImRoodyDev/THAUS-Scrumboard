package com.thaus.chatbox.classes;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	private String userId;
	private StringProperty username = new javafx.beans.property.SimpleStringProperty();
	private ObservableList<Group> groups = FXCollections.observableArrayList();
	private ObservableList<StringProperty> notifications = FXCollections.observableArrayList();

	public User(String userId, String username) {
		this.userId = userId;
		this.username.set(username);
	}

	public StringProperty getUsername() {
		return username;
	}

	public String getUserId() {
		return userId;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void removeGroup(Group group) {
		group.cleanup(); // Ensure listeners and resources are cleaned up
		groups.remove(group);
	}

	public ObservableList<Group> getGroups() {
		return groups;
	}

	public void cleanup() {

	}
}
