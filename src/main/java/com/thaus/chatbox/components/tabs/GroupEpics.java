package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.components.interactive.buttons.EpicButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class GroupEpics extends VBox implements ICustomNode {
	@FXML
	private Label featureLabel;
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
	private String featureId;
	private Runnable onFeatureClickHandler;
	private OnEpicClickHandler onEpicClickHandler;
	private ListChangeListener<Epic> featureListChangeListener = change -> {
		while (change.next()) {
			if (change.wasAdded()) {
				for (Epic feature : change.getAddedSubList()) {
					createEpicComponent(feature);
				}
			}

			if (change.wasRemoved()) {
				for (Epic feature : change.getRemoved()) {
					// Remove the feature from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof EpicButton epicButton) {
							return epicButton.getEpic().equals(feature);
						}
						return false;
					});
				}
			}
		}
	};

	// Constructor
	public GroupEpics() {
		initializeFXML("/components/tabs/group-epics.fxml");
	}

	@FXML
	public void initialize() {
		Feature feature = UserController.getChatController().currentFeature();
		this.groupId = UserController.getChatController().currentGroup().getId();
		this.featureId = feature.getId();

		// Initialize labels
		initializeLabels(feature);
		// Initialize the chat epics component
		initializeEpics(feature);
	}

	// Initialize labels
	private void initializeLabels(Feature currentFeature) {
		int featureIndex = UserController.getChatController().currentGroup().getFeatures().indexOf(currentFeature);
		this.featureLabel.setText("Feature " + (featureIndex + 1));
	}

	// Initialize the Epics list
	private void initializeEpics(Feature currentFeature) {
		// Check if epics is null or empty
		if (currentFeature.getEpics() == null || currentFeature.getEpics().isEmpty()) {
			dialog.setVisible(true);
		} else {
			if (viewContainer.getChildren().contains(dialog)) {
				viewContainer.getChildren().remove(dialog);
			}
		}

		// Clear the view container and add each epic to it
		viewContainer.getChildren().clear();

		// Add the listener to the epics list
		currentFeature.getEpics().addListener(featureListChangeListener);

		// Add each epic to the view container
		for (Epic feature : currentFeature.getEpics()) {
			createEpicComponent(feature);
		}

		// Add the create button action
		createButton.setOnAction(_ -> createEpic());

	}

	private void createEpic() {
		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}

		System.out.println("Creating epic: " + textField.getText());

		JSONObject response = UserController.createEpic(
				groupId,
				featureId,
				textField.getText()
		);

		if (response == null || response.getInt("statusCode") > 203) {
			return;
		} else {
			JSONObject epic = response.getJSONObject("epic");
			String epicId = epic.getString("id");
			String epicName = epic.getString("name");
			Feature currentFeature = UserController.getChatController().currentFeature();

			// Create a new epic
			currentFeature.addEpic(new Epic(epicId, epicName));
			// Clear the text field
			textField.clear();
		}
	}

	private void deleteEpic(Epic epic) {
		// Check if the epic is null
		if (epic == null) {
			return;
		}

		System.out.println("Deleting epic: " + epic.getName());

		JSONObject response = UserController.deleteEpic(
				groupId,
				featureId,
				epic.getId()
		);

		if (response == null || response.getInt("statusCode") > 203) {
			return;
		} else {
			// Remove the epic from the feature
			Feature currentFeature = UserController.getChatController().currentFeature();
			currentFeature.removeEpic(epic);
		}
	}

	private void createEpicComponent(Epic epic) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new epic component and add it to the view container
		EpicButton epicButton = new EpicButton(epic);
		epicButton.handleEpicClick(() -> {
			if (onEpicClickHandler != null) {
				onEpicClickHandler.onEpicClick(epic);
			}
		});
		epicButton.handleDeleteClick(() -> deleteEpic(epic));
		viewContainer.getChildren().add(epicButton);
	}

	public void handleFeatureClick(Runnable onFeatureClickHandler) {
		this.onFeatureClickHandler = onFeatureClickHandler;
		featureLabel.setOnMouseClicked(_ -> {
			System.out.println("Touch pressed");
			if (onFeatureClickHandler != null) {
				onFeatureClickHandler.run();
			}
		});
	}

	public void handleEpicClick(OnEpicClickHandler onEpicClickHandler) {
		this.onEpicClickHandler = onEpicClickHandler;
	}

	public void cleanup() {
		// Remove the listener from the features list
		viewContainer.getChildren().clear();
		onEpicClickHandler = null;
		onFeatureClickHandler = null;
		createButton.setOnAction(null);
		featureLabel.setOnMouseClicked(null);
		Feature currentFeature = UserController.getChatController().currentFeature();
		currentFeature.getEpics().removeListener(featureListChangeListener);
	}

	public interface OnEpicClickHandler {
		void onEpicClick(Epic featureEpics);
	}
}
