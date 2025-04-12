package com.thaus.chatbox.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.ChatboxButton;
import com.thaus.chatbox.components.CreateChatMenuComponent;
import com.thaus.chatbox.components.SidebarComponent;
import com.thaus.chatbox.components.WelcomeComponent;
import com.thaus.chatbox.controllers.ChatController;
import com.thaus.chatbox.interfaces.IChatListener;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.types.ChatboxType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class HomeController extends BaseScene {

	// Components inside the home fxml
	@FXML
	private BorderPane borderPane;
	@FXML
	private AnchorPane viewportContainer;

	// Custom Components
	private SidebarComponent sidebar;

	// Controllers
	private ChatController chatController;

	// Page values
	private HomeTab currentTab;
	private Object currentContentController;


	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();
		initializeSidebar();
		initializeChatController();
		System.out.println("Initialize Home controller's");

		// Going to the current page
		switchTab(HomeTab.WELCOME);
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onOpen() {

	}


	// Initialize sidebar component
	private void initializeSidebar() {
		// Create Sidebar component listeners
		sidebar = new SidebarComponent(
				new IChatboxFilterListener() {
					@Override
					public void onFilterApplied(ChatboxType type) {

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

					}

					@Override
					public void onTextChanged(String text) {

					}
				}
		);

		// Append in viewport
		borderPane.setLeft(sidebar);


		// On create chat new chat button
		sidebar.onCreateChatbox(() -> {
			switchTab(HomeTab.NEW_CHAT);
		});
	}

	// Initialize chat controller
	private void initializeChatController() {
		// Create the chat controller listener
		IChatListener chatListener = new IChatListener() {
			@Override
			public void initializeChatboxs(ArrayList<ChatboxButton> chatboxButtons) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().addAll(chatboxButtons);
			}

			@Override
			public void newChatbox(ChatboxButton chatboxButton) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().addFirst(chatboxButton);
			}

			@Override
			public void deleteChatbox(ChatboxButton chatboxButton) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().remove(chatboxButton);
			}

			@Override
			public void onChatboxsUpdated(ArrayList<ChatboxButton> chatboxButtons) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Clear the current contentArea
				chatboxContentArea.getChildren().clear();

				// Add it in the chatbox
				chatboxContentArea.getChildren().addAll(chatboxButtons);
			}
		};

		// Create the chat controller with the listener
		chatController = new ChatController(chatListener);
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
			FXMLLoader root;

			switch (tabName) {
				case WELCOME:
					WelcomeComponent welcomeComponent = new WelcomeComponent("Hello Roody");
					newContent = welcomeComponent;
					break;
				case NEW_CHAT:
					CreateChatMenuComponent createChatMenu = new CreateChatMenuComponent();

					// On cancel menu
					createChatMenu.setOnCancelAction(() -> {
						switchTab(HomeTab.WELCOME);
					});

					// Cn submit to create a new chatbox
					createChatMenu.setOnActionSubmit((ChatboxType type, String name) -> {
						chatController.createNewChatbox(type, name);
					});

					// Add it to the temporary content
					newContent = createChatMenu;
					break;
				case CHAT:

					break;
			}

			if (newContent != null) {
				viewportContainer.getChildren().clear();
				viewportContainer.getChildren().add(newContent);
				AnchorPane.setTopAnchor(newContent, 0.0);
				AnchorPane.setBottomAnchor(newContent, 0.0);
				AnchorPane.setLeftAnchor(newContent, 0.0);
				AnchorPane.setRightAnchor(newContent, 0.0);
			}
			currentTab = tabName;
			System.out.println("Switching tab: " + tabName);
		} catch (Exception e) {
			System.err.println("Error loading tab: " + tabName);
			e.printStackTrace();
		}
	}


	// Type of Tabs in home page
	public enum HomeTab {
		WELCOME,
		NEW_CHAT,
		CHAT,
	}
}
