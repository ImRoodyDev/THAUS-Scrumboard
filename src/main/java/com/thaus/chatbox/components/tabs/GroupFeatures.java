package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.components.interactive.buttons.FeatureButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class GroupFeatures extends VBox implements ICustomNode {
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

	// Observable properties
	private String groupId;
	private OnFeatureClickHandler onFeatureClickHandler;
	private ListChangeListener<Feature> featureListChangeListener = change -> {
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
	public GroupFeatures() {
		initializeFXML("/components/tabs/group-features.fxml");
	}

	@FXML
	public void initialize() {
		Group group = UserController.getChatController().currentGroup();
		this.groupId = group.getId();
		initializeFeatures(group);
	}

	// Initialize the feature list
	private void initializeFeatures(Group currentGroup) {
		// Check if features is null or empty
		if (currentGroup.getFeatures().isEmpty()) {
			dialog.setVisible(true);
		} else {
			if (viewContainer.getChildren().contains(dialog)) {
				viewContainer.getChildren().remove(dialog);
			}
		}

		// Add the listener to the features list
		currentGroup.getFeatures().addListener(featureListChangeListener);

		// Add each feature to the view container
		for (Feature feature : currentGroup.getFeatures()) {
			createFeatureComponent(feature);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createFeature());
	}

	private void createFeature() {
		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}

		System.out.println("Creating feature: " + textField.getText());

		// Create a new feature
		JSONObject response = UserController.createFeature(groupId, textField.getText());

		if (response == null || response.getInt("statusCode") > 203) {
			String message = response.getString("message");
			System.out.println(message);
		} else {
			JSONObject feature = response.getJSONObject("feature");
			String featureId = feature.getString("id");
			String featureName = feature.getString("name");
			Group currentGroup = UserController.getChatController().currentGroup();
			currentGroup.addFeature(new Feature(featureId, featureName));

			// Clear the text field
			textField.clear();
		}
	}

	private void deleteFeature(Feature feature) {
		// Check if the feature is null
		if (feature == null) {
			return;
		}

		System.out.println("Deleting feature: " + feature.getName());

		// Delete the feature
		JSONObject response = UserController.deleteFeature(groupId, feature.getId());

		if (response == null || response.getInt("statusCode") > 203) {
			return;
		} else {
			UserController.getChatController().currentGroup().removeFeature(feature);
		}
	}

	private void createFeatureComponent(Feature feature) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new feature button
		FeatureButton featureButton = new FeatureButton(feature);
		featureButton.handleOnClick(() -> {
			// Call the on feature click handler
			if (onFeatureClickHandler != null) {
				onFeatureClickHandler.onFeatureClick(feature);
			}
		});
		featureButton.handleOnDelete(() -> deleteFeature(feature));

		// Add it to the view container
		viewContainer.getChildren().add(featureButton);
	}

	public void handleFeatureClick(OnFeatureClickHandler onFeatureClickHandler) {
		this.onFeatureClickHandler = onFeatureClickHandler;
	}

	public void cleanup() {
		// Remove the listener
		viewContainer.getChildren().clear();
		onFeatureClickHandler = null;
		createButton.setOnAction(null);
		UserController.getChatController().currentGroup().getFeatures().removeListener(featureListChangeListener);
	}

	public interface OnFeatureClickHandler {
		void onFeatureClick(Feature feature);
	}
}
