package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Chat;
import com.thaus.chatbox.controllers.ChatboxFilterController;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class SidebarComponent extends AnchorPane implements ICustomNode {
	// Toggle searchbar element
	private boolean isSearchOpen = false;
	// Toggle filters pop up
	private boolean isFilterOpen = false;
	// Event listener for the Chatbox filter
	private IChatboxFilterListener filterListener;
	// Event listener for the chatbox search
	private ISearchListeners searchListeners;
	// Filter controller
	private ChatboxFilterController filterController;

	// Search bar controller
	@FXML
	private SearchBarComponent searchBar;
	// Sidebar components
	@FXML
	private FlowPane filtersFlowPane;
	@FXML
	private VBox separatorBar;
	@FXML
	private JFXButton filterBtn;
	@FXML
	private JFXButton searchBtn;
	@FXML
	private VBox chatboxsScrollContent;
	@FXML
	private JFXButton newChatboxBtn;

	// Default constructor needed for JavaFX
	public SidebarComponent() {
		initializeFXML("/components/interactive/sidebar.fxml");
	}

	// Constructor for manually loading the sidebar
	public SidebarComponent(IChatboxFilterListener filterListener, ISearchListeners searchListeners) {
		initializeFXML("/components/interactive/sidebar.fxml");
		initializeListeners(filterListener, searchListeners);
	}

	@FXML
	public void initialize() {
		// Initialize buttons
		initializeButtons();

		// Initialize Chatbox
		initializeChatboxs();
	}

	// Initialize Filter controller
	public void initializeListeners(IChatboxFilterListener filterListener, ISearchListeners searchListeners) {
		this.filterListener = filterListener;
		this.searchListeners = searchListeners;
		initializeSearchBar();
		initializeFilters();
	}

	public ChatboxButton createChatboxs(Chat chatbox) {
		ChatboxButton newChatboxBtn = new ChatboxButton(chatbox);

		// Add the new chatbox button
		chatboxsScrollContent.getChildren().add(newChatboxBtn);

		return newChatboxBtn;
	}

	public void removeChatbox(Chat chat) {
		VBox chatsContentArea = getChatboxsScrollContent();
		chatsContentArea.getChildren().removeIf(node ->
				node instanceof ChatboxButton && ((ChatboxButton) node).getChat().equals(chat)
		);
	}

	// Chatboxs scroll content
	public VBox getChatboxsScrollContent() {
		return chatboxsScrollContent;
	}

	// Set action on create chat
	public void onCreateChatbox(Runnable action) {
		newChatboxBtn.setOnAction(event -> action.run());
	}

	// Initialize filter bar
	private void initializeFilters() {
		// Initialize filter controller
		filterController = new ChatboxFilterController(filtersFlowPane, filterListener, true);
	}

	// Initialize searchbar
	private void initializeSearchBar() {
		searchBar = new SearchBarComponent(isSearchOpen, searchListeners);

		// Add element in the separator bar
		if (separatorBar != null) {
			separatorBar.getChildren().add(searchBar);
		} else {
			System.out.println("(SideBar) separatorBar is null");
		}
	}

	// Initialize buttons
	private void initializeButtons() {
		// Initialize filter button
		filterBtn.setOnAction(_ -> toggleFilter());

		// Initialize search button
		searchBtn.setOnAction(_ -> toggleSearch());
	}

	// Initialize chatbox
	private void initializeChatboxs() {
		// Clear chatbox content remove template
		chatboxsScrollContent.getChildren().clear();
	}

	// Toggle search bar
	private void toggleSearch() {
		isSearchOpen = !isSearchOpen;
		searchBar.enableComponent(isSearchOpen);
	}

	// Toggle Filter pop up
	private void toggleFilter() {
		isFilterOpen = !isFilterOpen;
		filterController.enableContextMenu(isFilterOpen);
	}

}
