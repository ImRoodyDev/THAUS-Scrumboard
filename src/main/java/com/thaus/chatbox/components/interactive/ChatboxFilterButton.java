package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.types.GroupType;
import javafx.fxml.FXMLLoader;

public class ChatboxFilterButton {
	// Type of filter
	private GroupType type;
	// Filter root button
	private JFXButton rootButton;
	// OnClick event
	private Runnable onClick;

	// Constructor
	public ChatboxFilterButton(GroupType filter) {
		try {
			// Save chat type
			this.type = filter;

			// Load the component FXML document
			FXMLLoader root = new FXMLLoader(getClass().getResource("/components/interactive/filter-button.fxml"));
			// Get the root object which is the JFXButton
			rootButton = root.load();

			// Apply the label
			rootButton.setText(filter.getName());
		} catch (Exception e) {
			System.out.printf("Failed to create filter button: %s\n\n %s", filter, e.getMessage());
		}
	}

	// Get filter type
	public GroupType getType() {
		return type;
	}

	// Get component
	public JFXButton getNode() {
		return rootButton;
	}

	// On Click event
	public void onClick(Runnable runnable) {
		this.onClick = runnable;
		rootButton.setOnAction(_ -> onClick.run());
	}

	// Event on destroy calling cleaning event then give you the node component
	public JFXButton onDestroy() {
		// If needed, remove listeners or references
		rootButton.setOnAction(null);

		// Return the node component
		return rootButton;
	}

}
