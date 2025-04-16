package com.thaus.chatbox.components.views;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.types.ScreenName;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

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

		// Validate input
		if (username.isEmpty() || password.isEmpty()) {
			triggerDialog("Please fill in all fields.");
		} else {
			JSONObject response = getUserController().login(username, password);
			if (response != null) {
				int statusCode = response.getInt("statusCode");

				if (statusCode == 200) {
					switchPage(ScreenName.Home);
				} else {
					triggerDialog(response.getString("message"));
				}
			} else {
				triggerDialog("Login failed. Please try again.");
			}
		}
	}

	public void onRegister() {
		String username = nameField.getText();
		String password = passwordField.getText();
		System.out.println("Register button clicked");

		// Validate input
		if (username.isEmpty() || password.isEmpty()) {
			triggerDialog("Please fill in all fields.");
		} else {
			JSONObject response = getUserController().register(username, password);

			if (response != null) {
				int statusCode = response.getInt("statusCode");

				if (statusCode == 200) {
					switchPage(ScreenName.Home);
				} else {
					triggerDialog(response.getString("message"));
				}
			} else {
				triggerDialog("Login failed. Please try again.");
			}
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
