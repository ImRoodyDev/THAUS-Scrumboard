package com.thaus.chatbox.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.*;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Collections;

public class HomeController extends BaseScene {
	// Components inside the home fxml
	@FXML
	private BorderPane borderPane;
	@FXML
	private AnchorPane viewportContainer;

	// Custom Components
	private SidebarComponent sidebar;

	// Page values
	private HomeTab currentTab;

	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();
		initializeSidebar();
		System.out.println("Initialize Home controller's");

		// Going to the current page
		switchTab(HomeTab.WELCOME);
	}

	@Override
	public void onOpen() {
		// Clear listener
		initializeChatController();
	}

	@Override
	public void onClose() {
		getChatController().removeChatboxesListener();
	}

	// Initialize sidebar component
	private void initializeSidebar() {
		// Create Sidebar component listeners
		sidebar = new SidebarComponent(
				new IChatboxFilterListener() {
					@Override
					public void onFilterApplied(ChatboxType type) {
						getChatController().filterChatboxes(Collections.singleton(type));
					}

					@Override
					public void onFilterRemoved(ChatboxType type) {

					}

					@Override
					public void onAllFiltersRemoved() {

					}
				},
				new ISearchListeners() {
					@Override
					public void onSubmit(String text) {
						getChatController().searchChatboxes(text);
					}

					@Override
					public void onTextChanged(String text) {
						System.out.println("Search Text changed to " + text);
					}
				}
		);

		// On create chat new chat button
		sidebar.onCreateChatbox(() -> switchTab(HomeTab.NEW_CHAT));

		// Append in viewport
		borderPane.setLeft(sidebar);
	}

	// Initialize chat controller
	private void initializeChatController() {
		// Get the observableList of the chatboxes
		ObservableList<ChatboxButton> buttons = getChatController().getChatboxButtons();

		// Initialize UI with current state
		VBox contentArea = sidebar.getChatboxsScrollContent();
		contentArea.getChildren().setAll(buttons);

		// Initialize chat controller listener
		getChatController().initialize(_ -> {
			contentArea.getChildren().setAll(buttons);
		});
		getChatController().setOnClickChatboxAction(() -> switchTab(HomeTab.CHAT));
	}

	// Switch to tab in the home page
	private void switchTab(HomeTab tabName) {
		if (currentTab == tabName) {
			System.out.println("You are already at tab: " + tabName);
			return;
		}

		try {
			// Loader for FXML
			Node newContent = null;

			switch (tabName) {
				case WELCOME:
					newContent = new WelcomeComponent("Hello Roody");
					break;
				case NEW_CHAT:
					CreateChatMenuComponent createChatMenu = new CreateChatMenuComponent();

					// On cancel menu
					createChatMenu.setOnCancelAction(() -> switchTab(HomeTab.WELCOME));

					// Cn submit to create a new chatbox
					createChatMenu.setOnActionSubmit((ChatboxType type, String name) -> {
						getChatController().createNewChatbox(type, name);
						switchTab(HomeTab.WELCOME);
					});

					// Add it to the temporary content
					newContent = createChatMenu;
					break;
				case CHAT:
					ChatComponent chatComponent = new ChatComponent(getChatController());
					newContent = chatComponent;
					break;
			}

			if (newContent != null) {
				// Clear contents and add new
				viewportContainer.getChildren().clear();
				viewportContainer.getChildren().add(newContent);

				// Set anchor position
				AnchorPane.setTopAnchor(newContent, 0.0);
				AnchorPane.setBottomAnchor(newContent, 0.0);
				AnchorPane.setLeftAnchor(newContent, 0.0);
				AnchorPane.setRightAnchor(newContent, 0.0);
				currentTab = tabName;
				System.out.println("Switching tab: " + tabName);
			}
		} catch (Exception e) {
			System.err.println("Error loading tab: " + tabName);
			// e.printStackTrace();
		}
	}

	// Type of Tabs in home page
	public enum HomeTab {
		WELCOME,
		NEW_CHAT,
		CHAT,
	}
}
