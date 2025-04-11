package com.thaus.chatbox.components;

import com.thaus.chatbox.controllers.FilterController;
import com.thaus.chatbox.interfaces.FilterListeners;
import com.thaus.chatbox.interfaces.SearchListeners;
import com.thaus.chatbox.types.ChatType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;

public class Sidebar extends AnchorPane {

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

	// Default constructor needed for JavaFX
	public Sidebar() {
		loadFXML();
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/side-bar.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
		}
	}

	// @FXML
	public void initializeww() {
		// Initialize the searchbar controller
		searchBar = new SearchBar(isSearchOpen, new SearchListeners() {
			@Override
			public void onSubmit(String text) {
				System.out.println("(SideBar) searched: " + text);
			}

			@Override
			public void onTextChanged(String text) {

			}
		});
		searchBar.initialize();

		// Initialize filter controller
		filterController = new FilterController(new FilterListeners() {
			@Override
			public void onFilterApplied(ChatType type) {
				System.out.println("(SideBar) filter applied: " + type);
			}

			@Override
			public void onFilterRemoved(ChatType type) {
				System.out.println("(SideBar) filter removed: " + type);
			}

			@Override
			public void onAllFiltersRemoved() {
				System.out.println("(SideBar) filters removed");
			}
		});
		filterController.initialize();

		// Initialize buttons
		initializeButtons();

		System.out.println("Sidebar initialized");
	}

	private void initializeButtons() {
		// Initialize filter button
		filterBtn.setOnAction(_ ->toggleFilter() );

		// Initialize search button
		searchBtn.setOnAction(_ ->toggleSearch() );
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
