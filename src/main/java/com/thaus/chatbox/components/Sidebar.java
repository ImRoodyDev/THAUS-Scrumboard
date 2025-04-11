package com.thaus.chatbox.components;

import com.thaus.chatbox.controllers.FilterController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXButton;

public class Sidebar {

	// Toggle searchbar element
	private boolean isSearchOpen = false;
	// Toggle filters pop up
	private boolean isFilterOpen = false;

	// Search bar controller
	private SearchBar searchBar;
	// Filter controller
	private FilterController filterController;

	// Sidebar components
	@FXML
	private JFXButton filterBtn;
	@FXML
	private JFXButton searchBtn;
	@FXML
	private ScrollPane chatboxScrollPane;
	@FXML
	private VBox chatboxScrollContent;
	@FXML
	private JFXButton newChatboxBtn;



	private void initializeButtons() {}
	private void toggleSearch(){}
	private void toggleFilter(){}
}
