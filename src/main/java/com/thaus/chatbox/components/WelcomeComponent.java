package com.thaus.chatbox.components;

import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class WelcomeComponent extends AnchorPane implements ICustomNode {

	// Cache font to avoid repeated loading
	private static Font customFont;
	@FXML
	private VBox textContainer;
	// Controller values
	private String welcomeText;

	// Constructor
	public WelcomeComponent(String text) {
		welcomeText = text;
		initializeFXML();
	}

	@FXML
	public void initialize() {
		// Load UI first
		Text placeholder = new Text(welcomeText);
		textContainer.getChildren().add(placeholder);

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
			textContainer.getChildren().clear();
			textContainer.getChildren().add(text);
		} catch (Exception e) {
			// Fallback if font loading fails
			System.err.println("Error loading custom font: " + e.getMessage());
		}
	}

	@Override
	public void initializeFXML() {
		// Load component
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/welcome-chat.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Sidebar FXML", e);
		}
	}
}


/*
		// Make the text thicker with stroke
		text.setStroke(javafx.scene.paint.Color.web("#0d9668")); // Slightly darker than your theme color
		text.setStrokeWidth(0.5);
		text.setStrokeType(StrokeType.OUTSIDE); // Makes it expand outward
*/
		/*
		// Add a glow effect to enhance thickness appearance
		Glow glow = new Glow();
		glow.setLevel(0.3);
		text.setEffect(glow);*/