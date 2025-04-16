package com.thaus.chatbox.base;

import com.thaus.chatbox.controllers.ChatController;
import com.thaus.chatbox.controllers.SceneController;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.IBaseScene;
import com.thaus.chatbox.types.ScreenName;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

public abstract class BaseScene implements IBaseScene {
	// Apply clipping to the parent AnchorPane
	Rectangle clip = new Rectangle();
	private double xOffset = 0;
	private double yOffset = 0;
	// Scene FXML Elements
	@FXML
	private AnchorPane root;
	@FXML
	private HBox dragArea;
	@FXML
	private Button closeWindowButton;
	@FXML
	private Button toggleMaximizeButton;
	@FXML
	private Button toggleMinimizeButton;
	@FXML
	private Label nameLabel;

	// Initialize automatically
	public void initialize() {
		if (nameLabel != null) {
			nameLabel.setText(getUserController().getUsername());
		}

		initializeBaseButtons();
		initializeDefaultStyling();
	}

	// Base scene buttons
	private void initializeBaseButtons() {
		// Close stage button
		if (closeWindowButton != null) {
			closeWindowButton.setOnAction(_ -> closeWindow()); // Attach close event
		}

		// Toggle Fullscreen button
		if (toggleMaximizeButton != null) {
			toggleMaximizeButton.setOnAction(_ -> toggleMaximize()); // Attach maximize toggle event
		}

		// Toggle Minimize button
		if (toggleMinimizeButton != null) {
			toggleMinimizeButton.setOnAction(_ -> minimize()); // Attach minimize event
		}

		// Event on dragging window
		if (dragArea != null) {
			// Record the initial position of the mouse on press
			dragArea.setOnMousePressed(event -> {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			});

			// Update the position of the window during drag
			dragArea.setOnMouseDragged(event -> {
				onScreenDrag(event.getScreenX() - xOffset, event.getScreenY() - yOffset);
			});
		}

	}

	// Apply default styling to the window
	private void initializeDefaultStyling() {
		if (root != null) {
			clip.widthProperty().bind(root.widthProperty());
			clip.heightProperty().bind(root.heightProperty());

			// Add border radius to the stage root element
			clip.setArcWidth(36);
			clip.setArcHeight(36);
			root.setClip(clip);
		}
	}


	// On Stage drag
	private final void onScreenDrag(double x, double y) {
		SceneController.getPrimaryStage().setX(x);
		SceneController.getPrimaryStage().setY(y);
	}

	// Toggle stage fullscreen
	private final void toggleMaximize() {
		SceneController.toggleMaximize();

		if (SceneController.isMaximized()) {
			// Add border radius to the stage root element
			clip.setArcWidth(0);
			clip.setArcHeight(0);
			root.setClip(clip);
		} else {
			// Add border radius to the stage root element
			clip.setArcWidth(36);
			clip.setArcHeight(36);
			root.setClip(clip);
		}
	}

	// Minimize stage
	private final void minimize() {
		SceneController.minimize();
	}

	// Close stage
	private void closeWindow() {
		SceneController.closeStage();
	}

	protected void switchPage(ScreenName screenName) {
		SceneController.switchStage(screenName);
	}

	protected ChatController getChatController() {
		return SceneController.getChatController();
	}

	protected UserController getUserController() {
		return SceneController.getAuthenticationController();
	}
}
