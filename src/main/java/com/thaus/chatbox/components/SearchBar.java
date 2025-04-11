package com.thaus.chatbox.components;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SearchBar extends HBox implements ICustomNode {
	// Searched text
	private String text;
	// Searchbar listener
	private final ISearchListeners searchListeners;

	// Searchbar components
	@FXML
	private TextField searchBarTextField;
	@FXML
	private JFXButton searchBarBtn;

	SearchBar(boolean enable, ISearchListeners searchListeners) {
		// Initialize fxml
		initializeFXML();

		// Apply the search listener
		this.searchListeners = searchListeners;

		// Weather to enable or disable the component
		enableComponent(enable);
	}

	@FXML
	public void initialize() {
		initializeEvents();
	}
	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/search-bar.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Searchbar FXML", e);
		}
	}
	// Function to enable and disable components
	public void enableComponent(boolean enable){
		// Check if the search bar is not null
		if(searchBarBtn != null){
			// searchBarBtn.setDisable(!enable);
			this.setVisible(enable);
			this.setManaged(enable);
		}
		else {
			System.out.println("(SearchBar) Searchbar element is not found");
		}
	}
	// Get search text
	public String getText() {return text;}


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
}
