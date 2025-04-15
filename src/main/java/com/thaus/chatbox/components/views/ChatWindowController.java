package com.thaus.chatbox.components.views;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.*;
import com.thaus.chatbox.components.tabs.*;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.ChatThreadType;
import com.thaus.chatbox.types.ChatType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChatWindowController extends VBox implements ICustomNode {
	// Current
	private final Group currentGroup;

	// Components
	public AnchorPane windowContainer;
	public Label chatType;
	public Label chatLabel;
	public MenuButton tabsMenu;
	public JFXButton addButton;
	public JFXButton searchButton;

	// Component controls values
	private Object currentWindowType;
	private Runnable onWindowSwitch;

	// Holders
	private Feature currentFeature;
	private Epic currentEpic;
	private UserStory currentUserStory;

	// Constructor
	public ChatWindowController(Group chat) {
		this.currentGroup = chat;
		initializeFXML("/components/tabs/chat-window.fxml");
		// Initialize the chat window
		switchWindow(ChatThreadType.GENERAL);
	}

	@FXML
	public void initialize() {
		// Initialize the chat window menu
		initializeMenu();

		// Set chat label
		chatLabel.setText(currentGroup.getChatName());

		if (!currentGroup.isOwner()) {
			addButton.setVisible(false);
			addButton.setManaged(false);
		} else {
			addButton.setVisible(true);
			addButton.setManaged(true);
		}

	}

	// Initialize the chat window menu
	private void initializeMenu() {
		// Clear the tab menu add custom items
		tabsMenu.getItems().clear();
		ObservableList<MenuItem> tabMenuChildren = tabsMenu.getItems();

		for (ChatThreadType chatThreadType : ChatThreadType.values()) {
			MenuItem menuItem = new MenuItem(chatThreadType.getName());
			menuItem.setOnAction(event -> switchWindow(chatThreadType));
			tabMenuChildren.add(menuItem);
		}
	}

	private void switchWindow(ChatThreadType windowType) {
		if (this.currentWindowType == windowType) {
			// System.out.println("You are already at the: " + windowType.getName());
			return;
		}

		// Before changing the window, call the cleanup method
		if (onWindowSwitch != null) {
			onWindowSwitch.run();
			onWindowSwitch = null;
		}

		try {
			// Loader for FXML
			Node newComponent = null;

			switch (windowType) {
				case GENERAL:
					ChatGeneral chatGeneral = new ChatGeneral(currentGroup);

					// Setup controller values
					onWindowSwitch = chatGeneral::cleanup;
					newComponent = chatGeneral;
					searchButton.setVisible(true);
					searchButton.setManaged(true);
					break;
				case FEATURES:
					GroupFeatures groupFeatures = new GroupFeatures(currentGroup);
					groupFeatures.setOnFeatureClickHandler((Feature feature) -> {
						switchWindow(ExtraWindowType.EPICS, feature);
					});

					// Setup controller values
					newComponent = groupFeatures;
					onWindowSwitch = groupFeatures::cleanup;
					searchButton.setVisible(false);
					searchButton.setManaged(false);
					break;
				case SPRINTS:
					GroupSprints groupSprints = new GroupSprints(currentGroup);
					groupSprints.setOnSprintClickHandler((sprint) -> {
						switchWindow(ExtraWindowType.SPRINT_CHAT, sprint);
					});

					// Setup controller values
					onWindowSwitch = groupSprints::cleanup;
					newComponent = groupSprints;
					searchButton.setVisible(false);
					searchButton.setManaged(false);
					break;
			}

			// Update the window type
			if (newComponent != null) {
				// Clear contents and add new
				windowContainer.getChildren().clear();
				windowContainer.getChildren().add(newComponent);

				// Set anchor position
				AnchorPane.setTopAnchor(newComponent, 0.0);
				AnchorPane.setBottomAnchor(newComponent, 0.0);
				AnchorPane.setLeftAnchor(newComponent, 0.0);
				AnchorPane.setRightAnchor(newComponent, 0.0);

				// Set the window type
				currentWindowType = windowType;
				chatType.setText(windowType.getName());
				System.out.println("Switching tab: " + windowType.getName());
			}

		} catch (Exception e) {
			System.out.println("Failed to switch window: " + windowType.getName());
			// e.printStackTrace();
		}
	}

	private void switchWindow(ExtraWindowType windowType, Object object) {
		if (this.currentWindowType == windowType) {
			// System.out.println("You are already at the: " + windowType.name());
			return;
		}

		// Before changing the window, call the cleanup method
		if (onWindowSwitch != null) {
			onWindowSwitch.run();
			onWindowSwitch = null;
		}

		try {
			// Loader for FXML
			Node newComponent = null;

			switch (windowType) {
				case USER_STORY:
					GroupUserStories groupUserStories = new GroupUserStories((Epic) object, currentFeature);
					groupUserStories.setOnUserStoryClickHandler((userStory -> {
						currentEpic = (Epic) object;
						switchWindow(ExtraWindowType.STORY_CHAT, userStory);
					}));

					// Setup controller values
					newComponent = groupUserStories;
					onWindowSwitch = groupUserStories::cleanup;
					chatType.setText(ChatType.USER_STORY.getName());
					break;
				case EPICS:
					GroupEpics groupEpics = new GroupEpics((Feature) object);
					groupEpics.setOnEpicClickHandler((epic -> {
						currentFeature = (Feature) object;
						switchWindow(ExtraWindowType.USER_STORY, epic);
					}));

					// Setup controller values
					newComponent = groupEpics;
					onWindowSwitch = groupEpics::cleanup;
					chatType.setText(ChatType.EPICS.getName());
					break;
				case STORY_CHAT:
					UserStoryChat chatUserStory = new UserStoryChat(currentEpic, currentFeature, (UserStory) object);

					onWindowSwitch = chatUserStory::cleanup;
					newComponent = chatUserStory;
					break;
				case SPRINT_CHAT:
					SprintChat sprintChat = new SprintChat((Sprint) object);

					// Setup controller values
					onWindowSwitch = sprintChat::cleanup;
					newComponent = sprintChat;
					chatType.setText(ChatType.SPRINTS.getName());
					break;
			}

			// Update the window type
			if (newComponent != null) {
				// Clear contents and add new
				windowContainer.getChildren().clear();
				windowContainer.getChildren().add(newComponent);

				// Set anchor position
				AnchorPane.setTopAnchor(newComponent, 0.0);
				AnchorPane.setBottomAnchor(newComponent, 0.0);
				AnchorPane.setLeftAnchor(newComponent, 0.0);
				AnchorPane.setRightAnchor(newComponent, 0.0);

				// Set the window type
				currentWindowType = windowType;
				System.out.println("Switching tab: " + windowType);
			} else {
				// Clear contents and add new
				windowContainer.getChildren().clear();
			}

		} catch (Exception e) {
			System.out.println("Failed to switch window: Epics");
			// e.printStackTrace();
		}
	}

	private enum ExtraWindowType {
		USER_STORY,
		EPICS,
		STORY_CHAT,
		SPRINT_CHAT;
	}

}
