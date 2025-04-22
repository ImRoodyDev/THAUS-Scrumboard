package com.thaus.chatbox.controllers;

import com.thaus.chatbox.classes.*;
import com.thaus.chatbox.components.views.ChatWindowController;
import com.thaus.chatbox.utils.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// This class is responsible for managing the chat functionality and saving it in database
public class GroupController {
	// Selected chat
	private Group selectedGroup;
	private Feature selectedFeature;
	private Epic selectedEpic;
	private Story selectedStory;
	private Sprint selectedSprint;
	private ChatWindowController chatWindowController;

	// Get selected group
	public Group currentGroup() {
		return selectedGroup;
	}

	public Feature currentFeature() {
		return selectedFeature;
	}

	public Epic currentEpic() {
		return selectedEpic;
	}

	public Story currentStory() {
		return selectedStory;
	}

	public Sprint currentSprint() {
		return selectedSprint;
	}

	public void deselectCurrentGroup() {
		if (selectedGroup != null) {
			// Clean the chat window controller just in case if it's there
			if (chatWindowController != null) {
				chatWindowController.cleanup();
			}
			selectedGroup = null;
			selectedFeature = null;
			selectedEpic = null;
			selectedStory = null;
			selectedSprint = null;
		}
	}

	public void selectGroup(Group group) {
		if (selectedGroup == group) {
			System.out.println("Group already selected");
			return;
		}

		// Deselect the first group making sure everything is cleaned up
		deselectCurrentGroup();

		// Then select the new group
		selectedGroup = group;

		if (group == null) {
			System.out.println("Group is null");
			return;
		}

		// Check if group is already initialized
		if (!group.isInitialized()) {
			// Send server request
			JSONObject response = UserController.getGroup(group.getId());

			// Check if the response is null
			if (response == null) {
				System.out.println("Failed to get group");
				return;
			}

			// Check if the response is valid
			if (response.getInt("statusCode") < 203) {
				JSONObject groupData = response.getJSONObject("group");
				JSONArray members = groupData.getJSONArray("members");
				JSONArray sprints = groupData.getJSONArray("sprints");
				JSONArray features = groupData.getJSONArray("features");
				JSONArray messages = groupData.getJSONArray("messages");

				// Initialize members
				ArrayList<Member> memberList = initializeMembers(members);
				group.addMembers(memberList);

				// Initialize sprints
				ArrayList<Sprint> sprintList = initializeSprints(sprints);
				group.addSprints(sprintList);

				// Initialize features
				ArrayList<Feature> featureList = initializeFeatures(features);
				group.addFeatures(featureList);

				// Initialize messages
				ArrayList<Message> messageList = initializeMessages(messages);
				group.addMessages(messageList);

				group.setInitialized(true);
			}
		}

		if (chatWindowController != null)
			chatWindowController.changeChatWindow(group);
	}

	public void selectGroup(Group group, Runnable handler) {
		if (selectedGroup == group) {
			return;
		}
		selectGroup(group);
		handler.run();
	}

	public void selectedSprint(Sprint sprint) {
		this.selectedSprint = sprint;
		if (sprint.isMessageInitialized()) {
			return;
		}
		// Initialize messages if not already initialized
		JSONObject response = UserController.getSprintMessages(selectedGroup.getId(), sprint.getId());
		if (response == null) {
			System.out.println("Failed to get sprint messages");
			return;
		}
		if (response.getInt("statusCode") <= 203) {
			JSONArray messages = response.getJSONArray("messages");
			ArrayList<Message> messageList = initializeMessages(messages);
			sprint.addMessages(messageList);
			sprint.setMessageInitialized(true);
		}
	}

	public void selectedFeature(Feature feature) {
		this.selectedFeature = feature;
	}

	public void selectedEpic(Epic epic) {
		this.selectedEpic = epic;
	}

	public void selectedStory(Story story) {
		this.selectedStory = story;
		if (story.isMessageInitialized()) {
			return;
		}
		// Initialize messages if not already initialized
		JSONObject response = UserController.getStoryMessages(selectedGroup.getId(), story.getId());
		if (response == null) {
			System.out.println("Failed to get story messages");
			return;
		}
		if (response.getInt("statusCode") <= 203) {
			JSONArray messages = response.getJSONArray("messages");
			ArrayList<Message> messageList = initializeMessages(messages);
			story.addMessages(messageList);
			story.setMessageInitialized(true);
		}
	}

	public void setChatWindowController(ChatWindowController chatWindowController) {
		this.chatWindowController = chatWindowController;
	}

	public void cleanup() {
		deselectCurrentGroup();
		if (chatWindowController != null)
			chatWindowController.cleanup();
	}

	// Create array of selected group members
	private ArrayList<Member> initializeMembers(JSONArray members) {
		ArrayList<Member> memberList = new ArrayList<>();

		for (int i = 0; i < members.length(); i++) {
			JSONObject member = members.getJSONObject(i);
			String memberId = member.getString("userId");
			String memberName = member.getString("username");
			String role = member.getString("role");
			String createdAt = member.getString("createdAt");

			// Add member to group
			memberList.add(new Member(
					memberId,
					memberName,
					DateUtils.parseAndFormatDate(createdAt),
					role
			));
		}

		return memberList;
	}

	// Create array of selected group sprints
	private ArrayList<Sprint> initializeSprints(JSONArray sprints) {
		ArrayList<Sprint> sprintList = new ArrayList<>();

		for (int i = 0; i < sprints.length(); i++) {
			JSONObject sprint = sprints.getJSONObject(i);
			String sprintId = sprint.getString("id");
			String startDate = sprint.isNull("startDate") ? null : sprint.getString("startDate");
			String endDate = sprint.isNull("endDate") ? null : sprint.getString("endDate");
			String createdAt = sprint.getString("createdAt");

			// Add sprint to group
			sprintList.add(new Sprint(
					"Sprint " + (i + 1),
					sprintId,
					DateUtils.parseAndFormatDate(startDate),
					DateUtils.parseAndFormatDate(endDate),
					DateUtils.parseAndFormatDate(createdAt)
			));
		}

		// Sort sprints by created date from oldest to newest
		sprintList.sort((s1, s2) -> {
			if (s1.getCreatedAt() == null || s2.getCreatedAt() == null) {
				return 0;
			}
			return s1.getCreatedAt().compareTo(s2.getCreatedAt());
		});

		// Set name base on index
		for (int i = 0; i < sprintList.size(); i++) {
			sprintList.get(i).getName().set("Sprint " + (i + 1));
		}

		return sprintList;
	}

	// Initialize features epics user story
	private ArrayList<Feature> initializeFeatures(JSONArray features) {
		ArrayList<Feature> featureList = new ArrayList<>();

		for (int f = 0; f < features.length(); f++) {
			JSONObject feature = features.getJSONObject(f);
			String featureId = feature.getString("id");
			String name = feature.getString("name");

			// Create feature epics
			JSONArray epics = feature.getJSONArray("epics");
			ArrayList<Epic> epicList = new ArrayList<>();
			for (int e = 0; e < epics.length(); e++) {
				JSONObject epic = epics.getJSONObject(e);
				String epicId = epic.getString("id");
				String epicName = epic.getString("name");

				// Create feature epic stories
				JSONArray stories = epic.getJSONArray("stories");
				ArrayList<Story> storyList = new ArrayList<>();
				for (int s = 0; s < stories.length(); s++) {
					JSONObject story = stories.getJSONObject(s);
					String storyId = story.getString("id");
					String storyName = story.getString("name");
					String storyDescription = story.getString("description");
					String storyUserId = !story.isNull("userId") ? story.getString("userId") : null;
					String sprintId = !story.isNull("sprintId") ? story.getString("sprintId") : null;
					String startDate = !story.isNull("startDate") ? story.getString("startDate") : null;
					String endDate = !story.isNull("endDate") ? story.getString("endDate") : null;

					// Get current Sprint
					Story newStory = new Story(
							storyId,
							storyName,
							storyDescription,
							sprintId,
							storyUserId
					);

					// Create user story
					storyList.add(newStory);

					// Initialize dates
					storyList.get(s).updateDates(
							DateUtils.parseAndFormatDate(startDate),
							DateUtils.parseAndFormatDate(endDate)
					);

					// Add to sprint if exists
					if (sprintId != null) {
						Sprint storySprint = currentGroup().getSprintById(sprintId);
						if (storySprint != null) {
							storySprint.addUserStory(newStory);
						}
					}
				}

				// Add stories to epic
				Epic newEpic = new Epic(epicId, epicName);
				newEpic.addStories(storyList);
				epicList.add(newEpic);
			}


			// Add epics to feature
			Feature newFeature = new Feature(featureId, name);
			newFeature.addEpics(epicList);
			featureList.add(newFeature);
		}

		return featureList;
	}

	// Initialize messages
	private ArrayList<Message> initializeMessages(JSONArray messages) {
		ArrayList<Message> messageList = new ArrayList<>();

		for (int i = 0; i < messages.length(); i++) {
			JSONObject message = messages.getJSONObject(i);
			String content = message.getString("message");
			String createdAt = message.getString("createdAt");
			String senderId = message.getString("userId");
			String senderName = message.getString("username");
			boolean isYours = UserController.getCurrentUser().getId().equals(senderId);

			messageList.add(new Message(
					senderName,
					content,
					DateUtils.parseAndFormatDate(createdAt),
					isYours
			));
		}

		// Sort messages by date from oldest to newest
		messageList.sort((m1, m2) -> {
			if (m1.getDate() == null || m2.getDate() == null) {
				return 0;
			}
			return m1.getDate().compareTo(m2.getDate());
		});

		return messageList;
	}

	// Get current user
	private User getCurrentUser() {
		return UserController.getCurrentUser();
	}
}

