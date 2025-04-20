package com.thaus.chatbox.components.views;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.components.tabs.*;
import com.thaus.chatbox.controllers.GroupController;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChatWindowController extends VBox implements ICustomNode {
	// Components
	@FXML
	public AnchorPane windowContainer;
	@FXML
	public Label chatType;
	@FXML
	public Label chatLabel;
	@FXML
	public MenuButton tabsMenu;
	@FXML
	public JFXButton searchButton;

	// Component controls values
	private GroupController groupController;
	private Object currentWindowController;
	private TabType currentWindowType;
	private Runnable onWindowSwitch;

	// Constructor
	public ChatWindowController() {
		// Initialize the controller
		this.groupController = UserController.getChatController();
		groupController.setChatWindowController(this);

		initializeFXML("/components/tabs/chat-window.fxml");
		switchWindow(TabType.GENERAL);
	}

	@FXML
	public void initialize() {
		// Initialize the chat window menu
		initializeTabMenu();

		// Set chat label
		chatLabel.textProperty().bind(groupController.currentGroup().getName());
	}

	public void changeChatWindow(Group group) {
		System.out.println("Changing chat window to: " + group.getName().get());

		if (currentWindowType != TabType.GENERAL) {
			switchWindow(TabType.GENERAL);
		}

		// Initialize component again
		initialize();
	}

	// Initialize the chat window menu
	private void initializeTabMenu() {
		// Clear the tab menu add custom items
		tabsMenu.getItems().clear();
		ObservableList<MenuItem> tabMenuChildren = tabsMenu.getItems();

		for (TabType tabType : TabType.values()) {
			if (tabType == TabType.SPRINT_CHAT || tabType == TabType.STORY_CHAT ||
					tabType == TabType.USER_STORY || tabType == TabType.EPICS) {
				continue;
			}
			MenuItem menuItem = new MenuItem(tabType.getName());
			menuItem.setOnAction(_ -> switchWindow(tabType));
			tabMenuChildren.add(menuItem);
		}
	}

	private void switchWindow(TabType windowType) {
		if (this.currentWindowType == windowType) {
			return;
		}

		// Before changing the window, call the cleanup method
		if (onWindowSwitch != null) {
			onWindowSwitch.run();
			onWindowSwitch = null;
		}

		try {
			switch (windowType) {
				case GENERAL:
					ChatGeneral chatGeneral = new ChatGeneral();

					// Setup controller values
					onWindowSwitch = chatGeneral::cleanup;
					currentWindowController = chatGeneral;
					searchButton.setVisible(true);
					searchButton.setManaged(true);
					break;
				case SPRINTS:
					GroupSprints groupSprints = new GroupSprints();
					groupSprints.handleSprintClick((sprint) -> {
						groupController.selectedSprint(sprint);
						switchWindow(TabType.SPRINT_CHAT);
					});

					// Setup controller values
					currentWindowController = groupSprints;
					onWindowSwitch = groupSprints::cleanup;
					searchButton.setVisible(false);
					searchButton.setManaged(false);
					break;
				case MEMBERS:
					GroupMembers groupMembers = new GroupMembers();

					// Setup controller values
					currentWindowController = groupMembers;
					onWindowSwitch = groupMembers::cleanup;
					searchButton.setVisible(false);
					searchButton.setManaged(false);
					break;
				case FEATURES:
					GroupFeatures groupFeatures = new GroupFeatures();
					groupFeatures.handleFeatureClick((Feature feature) -> {
						groupController.selectedFeature(feature);
						switchWindow(TabType.EPICS);
					});

					// Setup controller values
					currentWindowController = groupFeatures;
					onWindowSwitch = groupFeatures::cleanup;
					searchButton.setVisible(false);
					searchButton.setManaged(false);
					break;
				case EPICS:
					GroupEpics groupEpics = new GroupEpics();
					groupEpics.handleEpicClick((epic -> {
						groupController.selectedEpic(epic);
						switchWindow(TabType.USER_STORY);
					}));
					groupEpics.handleFeatureClick(() -> {
						switchWindow(TabType.FEATURES);
					});

					// Setup controller values
					currentWindowController = groupEpics;
					onWindowSwitch = groupEpics::cleanup;
					break;
				case USER_STORY:
					GroupStories groupStories = new GroupStories();
					groupStories.handleStoryClick((userStory -> {
						groupController.selectedStory(userStory);
						switchWindow(TabType.STORY_CHAT);
					}));
					groupStories.handleEpicClick(() -> {
						switchWindow(TabType.EPICS);
					});
					groupStories.handleFeatureClick(() -> {
						switchWindow(TabType.FEATURES);
					});

					// Setup controller values
					currentWindowController = groupStories;
					onWindowSwitch = groupStories::cleanup;
					break;
				case STORY_CHAT:
					StoryChat chatUserStory = new StoryChat();
					chatUserStory.handleStoryClick(() -> {
						switchWindow(TabType.USER_STORY);
					});
					chatUserStory.handleFeatureClick(() -> {
						switchWindow(TabType.FEATURES);
					});
					chatUserStory.handleEpicClick(() -> {
						switchWindow(TabType.EPICS);
					});

					// Setup controller values
					currentWindowController = chatUserStory;
					onWindowSwitch = chatUserStory::cleanup;
					break;
				case SPRINT_CHAT:
					SprintChat sprintChat = new SprintChat();

					// Setup controller values
					currentWindowController = sprintChat;
					onWindowSwitch = sprintChat::cleanup;
					break;
			}

			// Update the window type
			if (currentWindowController != null) {
				// Clear contents and add new
				windowContainer.getChildren().clear();
				windowContainer.getChildren().add((Node) currentWindowController);

				// Set anchor position
				AnchorPane.setTopAnchor((Node) currentWindowController, 0.0);
				AnchorPane.setBottomAnchor((Node) currentWindowController, 0.0);
				AnchorPane.setLeftAnchor((Node) currentWindowController, 0.0);
				AnchorPane.setRightAnchor((Node) currentWindowController, 0.0);

				// Set the window type
				currentWindowType = windowType;
				chatType.setText(windowType.getName());
			}

		} catch (Exception e) {
			System.out.println("Failed to switch window: " + windowType.getName());
			// e.printStackTrace();
		}
	}

	public void cleanup() {
		System.out.println("Cleaning up chat window controller");
		// Call the cleanup method of the current window controller
		chatLabel.textProperty().unbind();
		chatLabel.setText("");

		if (currentWindowType != null || currentWindowController != null) {
			switch (currentWindowType) {
				case GENERAL:
					((ChatGeneral) currentWindowController).cleanup();
					break;
				case SPRINTS:
					((GroupSprints) currentWindowController).cleanup();
					break;
				case FEATURES:
					((GroupFeatures) currentWindowController).cleanup();
					break;
				case EPICS:
					((GroupEpics) currentWindowController).cleanup();
					break;
				case USER_STORY:
					((GroupStories) currentWindowController).cleanup();
					break;
				case STORY_CHAT:
					((StoryChat) currentWindowController).cleanup();
					break;
				case SPRINT_CHAT:
					((SprintChat) currentWindowController).cleanup();
					break;
				case MEMBERS:
					((GroupMembers) currentWindowController).cleanup();
				case null:
					break;
			}
		}

		currentWindowType = null;
		onWindowSwitch = null;
		currentWindowController = null;
	}

	private enum TabType {
		GENERAL("General"),
		FEATURES("Features"),
		EPICS("Epics"),
		USER_STORY("User Stories"),
		STORY_CHAT("User Story Chat"),
		SPRINTS("Sprints"),
		SPRINT_CHAT("Sprint Chat"),
		MEMBERS("Members"),
		;

		// Constructed enum name
		TabType(String name) {
			this.name = name;
		}

		private final String name;

		public String getName() {
			return name;
		}
	}
}
