package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.components.interactive.buttons.ChatboxButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class SidebarComponent extends AnchorPane implements ICustomNode {
	// Toggle searchbar element
	private boolean isSearchOpen = false;

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


	@FXML
	public void initialize() {
		// Initialize buttons
		initializeButtons();

		// Initialize Chatbox
		initializeChatboxs();
		initializeListeners();
	}

	// Initialize Filter controller
	public void initializeListeners() {
		initializeSearchBar();
		initializeFilters();
	}

	public ChatboxButton createChatboxs(Group chatbox) {
		ChatboxButton newChatboxBtn = new ChatboxButton(chatbox);

		// Add the new chatbox button
		chatboxsScrollContent.getChildren().add(newChatboxBtn);

		return newChatboxBtn;
	}

	// Chatboxs scroll content
	public VBox getChatboxsScrollContent() {
		return chatboxsScrollContent;
	}

	// Remove chatbox
	public void removeChatbox(Group chat) {
		VBox chatsContentArea = getChatboxsScrollContent();
		chatsContentArea.getChildren().removeIf(node ->
				node instanceof ChatboxButton && ((ChatboxButton) node).getChat().equals(chat)
		);
	}

	// Set action on create chat
	public void setOnCreateChat(Runnable action) {
		newChatboxBtn.setOnAction(event -> action.run());
	}

	// Initialize filter bar
	private void initializeFilters() {
		// Initialize filter controller
		filtersFlowPane.getChildren().clear();
	}

	// Initialize searchbar
	private void initializeSearchBar() {
		searchBar = new SearchBarComponent(isSearchOpen, new ISearchListeners() {
			@Override
			public void onSubmit(String text) {
				System.out.println("Search submitted: " + text);
			}

			@Override
			public void onTextChanged(String text) {
				System.out.println("Search text changed: " + text);
			}
		});

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
		filterBtn.setVisible(false);
		filterBtn.setManaged(false);


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

}
