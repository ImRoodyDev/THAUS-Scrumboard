package com.thaus.chatbox.components;

import com.thaus.chatbox.controllers.ChatboxFilterController;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;

public class Sidebar extends AnchorPane implements ICustomNode {
	// Toggle searchbar element
	private boolean isSearchOpen = false;
	// Toggle filters pop up
	private boolean isFilterOpen = false;
	// Event listener for the Chatbox filter
	private IChatboxFilterListener filterListener;
	// Event listener for the chatbox search
	private ISearchListeners searchListeners;


	// Search bar controller
	@FXML
	private SearchBar searchBar;
	// Filter controller
	private ChatboxFilterController filterController;

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
	private ScrollPane chatboxsScrollPane;
	@FXML
	private VBox chatboxsScrollContent;
	@FXML
	private JFXButton newChatboxBtn;

	// Default constructor needed for JavaFX
	public Sidebar() {
		initializeFXML();
	}
	// Constructor for manually loading the sidebar
	public Sidebar(IChatboxFilterListener filterListener, ISearchListeners searchListeners) {
		initializeFXML();
		initializeListeners(filterListener, searchListeners);
  	}

	@FXML
	public void initialize() {
		// Initialize buttons
		initializeButtons();

		// Initialize Chatbox
		initializeChatboxs();
	}
 	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/sidebar.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
		}
	}
	// Initialize Filter controller
	public void initializeListeners(IChatboxFilterListener filterListener, ISearchListeners searchListeners) {
		this.filterListener = filterListener;
		this.searchListeners = searchListeners;
		initializeSearchBar();
		initializeFilters();
	}
	// Chatboxs scroll content
	public VBox getChatboxsScrollContent() {
		return chatboxsScrollContent;
	}


	// Initialize filter bar
	private void initializeFilters() {
		// Initialize filter controller
		filterController = new ChatboxFilterController(filtersFlowPane, filterListener , true);
	}
	// Initialize searchbar
	private void initializeSearchBar() {
		searchBar = new SearchBar(isSearchOpen, searchListeners);

		// Add element in the separator bar
		if(separatorBar != null) {
			separatorBar.getChildren().add(searchBar);
		}
		else {
			System.out.println("(SideBar) separatorBar is null");
		}
	}
	// Initialize buttons
	private void initializeButtons() {
		// Initialize filter button
		filterBtn.setOnAction(_ ->toggleFilter() );

		// Initialize search button
		searchBtn.setOnAction(_ ->toggleSearch() );
	}
	// Initialize chatbox
	private void initializeChatboxs() {
		// Clear chatbox content remove template
		chatboxsScrollContent.getChildren().clear();
	}
	// Toggle search bar
	private void toggleSearch(){
		isSearchOpen = !isSearchOpen;
		searchBar.enableComponent(isSearchOpen);
	}
	// Toggle Filter pop up
	private void toggleFilter(){
		isFilterOpen = !isFilterOpen;
		filterController.enableContextMenu(isFilterOpen);
	}

}
