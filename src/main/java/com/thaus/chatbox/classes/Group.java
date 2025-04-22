package com.thaus.chatbox.classes;

import com.thaus.chatbox.types.GroupType;
import com.thaus.chatbox.utils.DateUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class Group {

	// Class properties to assign to UI
	private String groupId;
	private Date createdAt;
	private GroupType type;
	private boolean isOwner;
	private boolean isInitialized = false;

	// Observable lists for messages, features, sprints, and members
	private StringProperty name = new javafx.beans.property.SimpleStringProperty();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	private ObservableList<Feature> features = FXCollections.observableArrayList();
	private ObservableList<Sprint> sprints = FXCollections.observableArrayList();
	private ObservableList<Member> members = FXCollections.observableArrayList();


	public Group(String groupId, String name, boolean isOwner, Date createdAt, GroupType type) {
		this.groupId = groupId;
		this.isOwner = isOwner;
		this.createdAt = createdAt;
		this.type = type;

		// Set name
		this.name.set(name);
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public String getId() {
		return groupId;
	}

	public GroupType getType() {
		return type;
	}

	public StringProperty getName() {
		return name;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public String getCreatedAt() {
		return DateUtils.formatDate(createdAt);
	}

	public Sprint getSprintById(String id) {
		for (Sprint sprint : sprints) {
			if (sprint.getId().equals(id)) {
				return sprint;
			}
		}
		return null;
	}

	public ObservableList<Member> getMembers() {
		return members;
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


	public void setInitialized(boolean initialized) {
		isInitialized = initialized;
	}

	public void addFeature(Feature feature) {
		features.add(feature);
	}

	public void addFeatures(ArrayList<Feature> features) {
		this.features.addAll(features);
	}

	public void removeFeature(Feature feature) {
		features.remove(feature);
	}

	public void addSprint(Sprint sprint) {
		sprints.add(sprint);
	}

	public void addSprints(ArrayList<Sprint> sprints) {
		this.sprints.addAll(sprints);
	}

	public void deleteSprint(Sprint sprint) {
		sprint.unlinkStories();
		sprints.remove(sprint);
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void addMessages(ArrayList<Message> messages) {
		this.messages.addAll(0, messages);
	}

	public void addMember(Member member) {
		members.add(member);
	}

	public void addMembers(ArrayList<Member> members) {
		this.members.addAll(members);
	}

	public void removeMember(Member member) {
		members.remove(member);
	}

	public void cleanup() {
		// Remove listeners or clear resources associated with this group
		name.unbind(); // Example: Unbind any bindings on the name property
		messages.clear(); // Clear messages if necessary
		features.clear(); // Clear features if necessary
		sprints.clear(); // Clear sprints if necessary
		members.clear(); // Clear members if necessary
	}
}
