package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBarComponent extends HBox implements ICustomNode {
	// Searchbar listener
	private final ISearchListeners searchListeners;
	// Searched text
	private String text;
	// Searchbar components
	@FXML
	private TextField searchBarTextField;
	@FXML
	private JFXButton searchBarBtn;

	// Constructor
	SearchBarComponent(boolean enable, ISearchListeners searchListeners) {
		// Initialize fxml
		initializeFXML("/components/interactive/search-bar.fxml");

		// Apply the search listener
		this.searchListeners = searchListeners;

		// Weather to enable or disable the component
		enableComponent(enable);
	}

	@FXML
	public void initialize() {
		initializeEvents();
	}


	// Function to enable and disable components
	public void enableComponent(boolean enable) {
		// Check if the search bar is not null
		if (searchBarBtn != null) {
			// searchBarBtn.setDisable(!enable);
			this.setVisible(enable);
			this.setManaged(enable);
		} else {
			System.out.println("(SearchBar) Searchbar element is not found");
		}
	}

	// Get search text
	public String getText() {
		return text;
	}


	// Initialize components events
	private void initializeEvents() {
		// Check if the search bar is not null
		if (searchBarTextField != null) {
			searchBarTextField.textProperty().addListener((_, _, newValue) -> onTextChange(newValue));
		}

		// Initialize the search button to fire onSubmit
		if (searchBarBtn != null) {
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
