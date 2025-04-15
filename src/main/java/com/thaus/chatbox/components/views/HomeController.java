package com.thaus.chatbox.components.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.classes.Chat;
import com.thaus.chatbox.components.interactive.ChatboxButton;
import com.thaus.chatbox.components.interactive.SidebarComponent;
import com.thaus.chatbox.components.tabs.CreateChatComponent;
import com.thaus.chatbox.components.tabs.WelcomeComponent;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.types.ChatboxType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HomeController extends BaseScene {
	// Components inside the home fxml
	@FXML
	private BorderPane borderPane;    // Chat change listener
	@FXML
	private AnchorPane viewportContainer;
	// Custom Components
	private SidebarComponent sidebar;
	// Page values
	private HomeTab currentTab;


	// ListChangeListener for chat
	private ListChangeListener<Chat> chatListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Chat chat : change.getAddedSubList()) {
					ChatboxButton chatboxButton = sidebar.createChatboxs(chat);

					// Handle click action on chatbox
					chatboxButton.onClickHandle(_ -> {
						getChatController().selectChatbox(chat);
						switchTab(HomeTab.CHAT);
					});

					// Handle delete action on chatbox
					chatboxButton.onDeleteAction(_ -> {
						// When delete a chat
						if (getChatController().getCurrentChat().equals(chat)) {
							switchTab(HomeTab.WELCOME);
						}
						// Remove chatbox from the sidebar
						getChatController().deleteChatbox(chat);
					});
				}

				// Select the last added chat
				getChatController().selectChatbox(change.getAddedSubList().getLast());
			}

			if (change.wasRemoved()) {
				for (Chat chat : change.getRemoved()) {
					sidebar.removeChatbox(chat); // Ensure this method removes the chat UI
				}
			}
		}
	};

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
		initializeChats();
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
	private void initializeChats() {
		// Get the observableList of the chatboxes
		ObservableList<Chat> chats = getChatController().getChats();

		// Initialize UI with current state
		VBox chatsContentArea = sidebar.getChatboxsScrollContent();
		chatsContentArea.getChildren().clear();

		// Add listener to array
		getChatController().setChatListListener(chatListChangeListener);

		// Get available chat
		for (Chat chat : getChatController().getChats()) {
			sidebar.createChatboxs(chat);
		}

		// Set action on chatbox selected
		// getChatController().setOnClickChatboxDeletedAction(() -> switchTab(HomeTab.WELCOME));
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
					CreateChatComponent createChatMenu = new CreateChatComponent();

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
					ChatWindowController chatComponent = new ChatWindowController(getChatController().getCurrentChat());
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
