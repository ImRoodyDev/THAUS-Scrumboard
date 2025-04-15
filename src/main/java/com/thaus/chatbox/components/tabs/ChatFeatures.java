package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Chat;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.components.interactive.FeatureButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatFeatures extends VBox implements ICustomNode {
	private final Chat currentChat;
	// FXML components // Text field for creating new feature
	@FXML
	private TextField textField;
	@FXML
	private JFXButton createButton;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;
	@FXML
	private VBox viewContainer;
	private OnFeatureClickHandler onFeatureClickHandler;
	private final ListChangeListener<Feature> chatFeatureListChangeListener = change -> {
		while (change.next()) {

			if (change.wasAdded()) {
				for (Feature feature : change.getAddedSubList()) {
					createFeatureComponent(feature);
				}
			}

			if (change.wasRemoved()) {
				for (Feature feature : change.getRemoved()) {
					// Remove the feature from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof FeatureButton featureButton) {
							return featureButton.getFeature().equals(feature);
						}
						return false;
					});
				}
			}
		}
	};
 
	// Constructor
	public ChatFeatures(Chat chat) {
		this.currentChat = chat;
		initializeFXML("/components/tabs/chat-features.fxml");
	}

	@FXML
	public void initialize() {
		initializeFeatures();

	}

	// Initialize the feature list
	private void initializeFeatures() {
		// Check if features is null or empty
		if (currentChat.getFeatures() == null || currentChat.getFeatures().isEmpty()) {
			dialog.setVisible(true);
		} else {
			if (viewContainer.getChildren().contains(dialog)) {
				viewContainer.getChildren().remove(dialog);
			}
		}

		// Add the listener to the features list
		currentChat.getFeatures().addListener(chatFeatureListChangeListener);

		// Add each feature to the view container
		for (Feature feature : currentChat.getFeatures()) {
			createFeatureComponent(feature);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createFeature());
	}

	private void createFeature() {
		System.out.println("Test create: " + textField.getText());

		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}

		System.out.println("Creating feature: " + textField.getText());

		// Create a new feature
		currentChat.createFeature(textField.getText());

		// Clear the text field
		textField.clear();
	}

	private void createFeatureComponent(Feature feature) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new feature button
		FeatureButton featureButton = new FeatureButton(feature);
		featureButton.setOnClickHandler(() -> {
			// Call the on feature click handler
			if (onFeatureClickHandler != null) {
				onFeatureClickHandler.onFeatureClick(feature);
			}
		});
		featureButton.setOnDeleteHandler(() -> currentChat.removeFeature(feature));

		// Add it to the view container
		viewContainer.getChildren().add(featureButton);
	}

	public void cleanup() {
		// Remove the listener
		currentChat.getFeatures().removeListener(chatFeatureListChangeListener);
	}

	public void setOnFeatureClickHandler(OnFeatureClickHandler onFeatureClickHandler) {
		this.onFeatureClickHandler = onFeatureClickHandler;
	}

	public interface OnFeatureClickHandler {
		void onFeatureClick(Feature feature);
	}

}
