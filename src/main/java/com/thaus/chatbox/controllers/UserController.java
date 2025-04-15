package com.thaus.chatbox.controllers;

import com.thaus.chatbox.types.ScreenName;

public class UserController {
	private boolean isLoggedIn = false;
	private String username;

	public UserController() {
		// Initialize the user controller
		initialize();
	}

	private void initialize() {
		// Initialize the user controller
		this.isLoggedIn = false;
		this.username = null;
	}

	public void login(String username, String password) {
		// Simulate a login process
		if (username != null && password != null) {
			this.username = username;
			this.isLoggedIn = true;
			SceneController.switchStage(ScreenName.Home);
		} else {
			throw new IllegalArgumentException("Username and password cannot be null");
		}
	}

	public void register(String username, String password) {
		// Simulate a registration process
		if (username != null && password != null) {
			this.username = username;
			this.isLoggedIn = true;
		} else {
			throw new IllegalArgumentException("Username and password cannot be null");
		}
	}

	public void logout() {
		this.username = null;
		this.isLoggedIn = false;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public String getUsername() {
		return username;
	}
}
