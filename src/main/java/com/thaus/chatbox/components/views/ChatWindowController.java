package com.thaus.chatbox.components.views;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Chat;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.components.tabs.ChatEpics;
import com.thaus.chatbox.components.tabs.ChatFeatures;
import com.thaus.chatbox.components.tabs.ChatGeneral;
import com.thaus.chatbox.components.tabs.ChatUserStories;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.ChatThreadType;
import com.thaus.chatbox.types.ChatType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChatWindowController extends VBox implements ICustomNode {
	// Current
	private final Chat currentChat;

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
	private Menu featureMenu;
	private Menu epicsMenu;
	private Menu userStoryMenu;


	// Constructor
	public ChatWindowController(Chat chat) {
		this.currentChat = chat;
		initializeFXML("/components/tabs/chat-window.fxml");
		// Initialize the chat window
		switchWindow(ChatThreadType.GENERAL);
	}

	@FXML
	public void initialize() {
		// Initialize the chat window menu
		initializeMenu();

		// Set chat label
		chatLabel.setText(currentChat.getChatName());

		if (!currentChat.isOwner()) {
			addButton.setVisible(false);
			addButton.setManaged(false);
		} else {
			addButton.setVisible(true);
			addButton.setManaged(true);
		}

	}

	// Initialize the chat window menu
	private void initializeMenu() {
		tabsMenu.getItems().clear();
		ObservableList<MenuItem> tabMenuChildren = tabsMenu.getItems();

		// Loop through all the chat type and add it in the dropdown
		ChatThreadType[] chatThreadTypes = ChatThreadType.values();

		for (ChatThreadType chatThreadType : chatThreadTypes) {
			if (chatThreadType == ChatThreadType.FEATURES) {
				// Create another menu
				featureMenu = new Menu(chatThreadType.getName());
				epicsMenu = new Menu(ChatType.EPICS.getName());
				userStoryMenu = new Menu(ChatType.USER_STORY.getName());

				// Add action event on the menu button
				featureMenu.setOnAction(event -> switchWindow(chatThreadType));
				epicsMenu.setOnAction(event -> switchWindow(ChatType.EPICS));
				userStoryMenu.setOnAction(event -> switchWindow(ChatType.USER_STORY));

				// Form the menu
				epicsMenu.getItems().add(userStoryMenu);
				featureMenu.getItems().add(epicsMenu);
				tabMenuChildren.add(featureMenu);

				return;
			}

			MenuItem menuItem = new MenuItem(chatThreadType.getName());
			menuItem.setOnAction(event -> {
				System.out.println("Selected : " + chatThreadType.getName());
				switchWindow(chatThreadType);
			});
			tabMenuChildren.add(menuItem);
		}
	}

	private void switchWindow(ChatThreadType windowType) {
		if (this.currentWindowType == windowType) {
			System.out.println("You are already at the: " + windowType.getName());
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
					ChatGeneral chatGeneral = new ChatGeneral(currentChat);
					onWindowSwitch = chatGeneral::cleanup;
					newComponent = chatGeneral;
					break;
				case FEATURES:
					ChatFeatures chatFeatures = new ChatFeatures(currentChat);
					onWindowSwitch = chatFeatures::cleanup;
					chatFeatures.setOnFeatureClickHandler((Feature feature) -> {
						switchWindow(ExtraWindowType.EPICS, feature);
					});
					newComponent = chatFeatures;
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
		}
	}


	private void switchWindow(ExtraWindowType windowType, Object object) {
		if (this.currentWindowType == windowType) {
			System.out.println("You are already at the: " + windowType.name());
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
					ChatUserStories chatUserStories = new ChatUserStories(object);
					onWindowSwitch = chatUserStories::cleanup;
					newComponent = chatUserStories;
					chatType.setText(ChatType.USER_STORY.getName());
					break;
				case EPICS:
					ChatEpics chatEpics = new ChatEpics(object);
					onWindowSwitch = chatEpics::cleanup;
					chatEpics.setOnEpicClickHandler((epic -> {
						switchWindow(ExtraWindowType.USER_STORY, epic);
					}));
					newComponent = chatEpics;
					chatType.setText(ChatType.EPICS.getName());

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
			}

		} catch (Exception e) {
			System.out.println("Failed to switch window: Epics");
		}
	}


	private enum ExtraWindowType {
		USER_STORY,
		EPICS,
		STORY_CHAT;
	}

}
