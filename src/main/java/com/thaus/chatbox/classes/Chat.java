package com.thaus.chatbox.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Chat {
	private String chatId;
	private String chatName;
	private String createdAt;
	private boolean isOwner;
	private ObservableList<Message> messages;
	private ObservableList<Feature> features;


	public Chat(String chatId, String chatName, boolean isOwner, String createdAt) {
		this.chatId = chatId;
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

	public ObservableList<Message> getMessages() {
		return messages;
	}

	public ObservableList<Feature> getFeatures() {
		return features;
	}

	public void createFeature(String featureName) {
		Feature feature = new Feature(this.chatId, featureName);
		features.add(feature);
	}

	public void removeFeature(Feature feature) {
		features.remove(feature);
	}

	public void editFeature(Feature feature) {
		int index = features.indexOf(feature);
		if (index != -1) {
			features.set(index, feature);
		}
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void removeMessage(Message message) {
		messages.remove(message);
	}

	public void editMessage(Message message) {
		int index = messages.indexOf(message);
		if (index != -1) {
			messages.set(index, message);
		}
	}

	public String getChatId() {
		return chatId;
	}

	public String getChatName() {
		return chatName;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public String getCreatedAt() {
		return createdAt;
	}
}
