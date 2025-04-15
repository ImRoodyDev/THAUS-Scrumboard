package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Epic;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.components.interactive.buttons.EpicButton;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GroupEpics extends VBox implements ICustomNode {
	private final Feature currentChatFeature;

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
	private OnEpicClickHandler onEpicClickHandler;
	private final ListChangeListener<Epic> chatFeatureListChangeListener = change -> {
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
	public GroupEpics(Feature chatFeature) {
		this.currentChatFeature = chatFeature;
		initializeFXML("/components/tabs/group-epics.fxml");
	}

	@FXML
	public void initialize() {
		// Initialize labels
		initializeLabels();
		// Initialize the chat epics component
		initializeEpics();
	}

	// Initialize labels
	private void initializeLabels() {
		this.featureLabel.setText(currentChatFeature.getName());
		this.featureLabel.setText(currentChatFeature.getName());
	}

	// Initialize the Epics list
	private void initializeEpics() {
		// Check if epics is null or empty
		if (currentChatFeature.getEpics() == null || currentChatFeature.getEpics().isEmpty()) {
			dialog.setVisible(true);
		} else {
			if (viewContainer.getChildren().contains(dialog)) {
				viewContainer.getChildren().remove(dialog);
			}
		}

		// Clear the view container and add each epic to it
		viewContainer.getChildren().clear();

		// Add the listener to the epics list
		currentChatFeature.getEpics().addListener(chatFeatureListChangeListener);

		// Add each epic to the view container
		for (Epic feature : currentChatFeature.getEpics()) {
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

		// Create a new feature
		currentChatFeature.createEpic(textField.getText());

		// Clear the text field
		textField.clear();
	}

	private void createEpicComponent(Epic epic) {
		if (viewContainer.getChildren().contains(dialog)) {
			viewContainer.getChildren().remove(dialog);
		}

		// Create a new epic component and add it to the view container
		EpicButton epicButton = new EpicButton(epic);
		epicButton.setOnClickHandler(() -> {
			if (onEpicClickHandler != null) {
				onEpicClickHandler.onEpicClick(epic);
			}
		});
		epicButton.setOnDeleteHandler(() -> currentChatFeature.deleteEpic(epic));
		viewContainer.getChildren().add(epicButton);
	}

	public void cleanup() {
		// Remove the listener from the features list
		currentChatFeature.getEpics().removeListener(chatFeatureListChangeListener);
	}

	public void setOnEpicClickHandler(OnEpicClickHandler onEpicClickHandler) {
		this.onEpicClickHandler = onEpicClickHandler;
	}

	public interface OnEpicClickHandler {
		void onEpicClick(Epic featureEpics);
	}
}
