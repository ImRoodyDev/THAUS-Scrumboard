package com.thaus.chatbox.components;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.interfaces.SearchListeners;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar {
	// Searched text
	private String text;
	// Searchbar listener
	private final SearchListeners searchListeners;

	// Searchbar components
	@FXML
	private HBox searchBar;
	@FXML
	private TextField searchBarTextField;
	@FXML
	private JFXButton searchBarBtn;

	SearchBar(boolean enable, SearchListeners searchListeners) {
		// Apply the search listener
		this.searchListeners = searchListeners;

		// Weather to enable or disable the component
		enableComponent(enable);
	}

	@FXML
	public void initialize() {
		initializeEvents();
		System.out.println("SearchBar Initialized");
	}

	// Initialize components events
	private void initializeEvents(){
		// Check if the search bar is not null
		if (searchBarTextField != null) {
			searchBarTextField.textProperty().addListener((_, _, newValue) -> onTextChange(newValue));
		}

		// Initialize the search button to fire onSubmit
		if(searchBarBtn != null){
			searchBarBtn.setOnAction(_ -> onSubmit());
		}
	}

	// Listen event for the text field component
	private void onTextChange(String text) {
		this.text = text;
		searchListeners.onTextChanged(text);
	}

	// Listen event for the onSubmit event
	private void onSubmit() {
		searchListeners.onSubmit(this.text);
	}

	// Function to enable and disable components
	public void enableComponent(boolean enable){

		// Check if the search bar is not null
		if(searchBar != null && searchBarBtn != null){
 			searchBarBtn.setDisable(!enable);
			searchBar.setVisible(enable);
			searchBar.setManaged(enable);
		}
		else {
			System.out.println("(SearchBar) Searchbar element is not found");
		}
	}
	// Get search text
	public String getText() {return text;}
}
