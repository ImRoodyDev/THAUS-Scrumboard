package com.thaus.chatbox.components.views;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.base.BaseScene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginRegisterController extends BaseScene {
	@FXML
	public TextField nameField;
	@FXML
	public TextField passwordField;
	@FXML
	public JFXButton loginButton;
	@FXML
	public JFXButton createAccount;
	@FXML
	public VBox dialogContainer;
	@FXML
	public JFXButton dialogButton;
	@FXML
	public Label dialogLabel;


	@Override
	public void initialize() {
		super.initialize();
		System.out.println("Initialize LoginRegister controller's");
		loginButton.setOnAction(event -> onLogin());
		createAccount.setOnAction(event -> onRegister());
	}

	public void onLogin() {
		String username = nameField.getText();
		String password = passwordField.getText();
		System.out.println("Login button clicked");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);

		// Validate input
		if (username.isEmpty() || password.isEmpty()) {
			triggerDialog("Please fill in all fields.");
		} else {

			getUserController().login(username, password);
			triggerDialog("Login successful!");
		}
	}

	public void onRegister() {
		String username = nameField.getText();
		String password = passwordField.getText();
		System.out.println("Register button clicked");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);


		// Validate input
		if (username.isEmpty() || password.isEmpty()) {
			triggerDialog("Please fill in all fields.");
		} else {
			getUserController().register(username, password);
			triggerDialog("Registration successful!");
		}
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onOpen() {

	}

	private void triggerDialog(String message) {
		dialogLabel.setText(message);
		dialogButton.setVisible(true);
		dialogButton.setOnAction(event -> {
			dialogButton.setVisible(false);
		});
	}
}
