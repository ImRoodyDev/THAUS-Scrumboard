package com.thaus.chatbox.components.tabs;

import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WelcomeComponent extends AnchorPane implements ICustomNode {

	// Cache font to avoid repeated loading
	private static Font customFont;
	// Controller values
	private final String welcomeText;
	@FXML
	private VBox textContainer;

	// Constructor
	public WelcomeComponent(String text) {
		welcomeText = text;
		initializeFXML("/components/tabs/welcome-chat.fxml");
	}

	@FXML
	public void initialize() {
		// Load font asynchronously to avoid blocking UI
		Platform.runLater(this::initializeFont);
	}

	public void initializeFont() {
		try {
			// Load font only once and reuse
			if (customFont == null) {
				customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Loverine.otf"), 72);
			}

			// Create text with your custom font
			Text text = new Text(welcomeText);
			text.setFont(customFont);

			// Create gradient fill
			LinearGradient gradient = new LinearGradient(
					0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
					new Stop(0, javafx.scene.paint.Color.web("#10b981")),
					new Stop(1, javafx.scene.paint.Color.web("#3b82f6"))
			);

			// Apply gradient to text
			text.setFill(gradient);

			// Clear previous placeholder and add styled text
			textContainer.getChildren().addFirst(text);
		} catch (Exception e) {
			// Fallback if font loading fails
			System.err.println("Error loading custom font: " + e.getMessage());
		}
	}


}
