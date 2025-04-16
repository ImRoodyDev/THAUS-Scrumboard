package com.thaus.chatbox.classes;

import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Group {
	// Class properties to assign to UI
	private String groupId;
	private String chatName;
	private String createdAt;
	private int unreadCount;
	private boolean isOwner;
	private ChatboxType type;

	// Observable lists for messages, features, sprints, and members
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	private ObservableList<Feature> features = FXCollections.observableArrayList();
	private ObservableList<Sprint> sprints = FXCollections.observableArrayList();
	private ObservableList<Member> members = FXCollections.observableArrayList();

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

	public Group(String groupId, String chatName, boolean isOwner, String createdAt) {
		this.groupId = groupId;
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

	public void initializeSprints(ArrayList<Sprint> sprints) {
		this.sprints = FXCollections.observableArrayList(sprints);
	}

	public void createSprint() {
		Sprint sprint = new Sprint();
		sprints.add(sprint);
	}

	public void createFeature(String featureName) {
		Feature feature = new Feature(this.groupId, featureName);
		features.add(feature);
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void addMember(Member member) {
		members.add(member);
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public void deleteSprint(Sprint sprint) {
		sprints.remove(sprint);
	}

	public void removeFeature(Feature feature) {
		features.remove(feature);
	}

	public boolean isOwner() {
		return isOwner;
	}

	public ChatboxType getType() {
		return type;
	}

	public String getChatName() {
		return chatName;
	}

	public int getUnreadCount() {
		return unreadCount;
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

}
