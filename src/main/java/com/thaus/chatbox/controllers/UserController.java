package com.thaus.chatbox.controllers;

import com.thaus.chatbox.classes.User;
import com.thaus.chatbox.types.ScreenName;
import com.thaus.chatbox.utils.FetchUtils;
import com.thaus.chatbox.utils.TokenUtils;
import org.json.JSONObject;

import java.util.Map;

public class UserController {
	private static User currentUser = null;
	private final String API = "http://localhost:3002/api";

	// UserController variables
	private boolean isLoggedIn = false;
	private ChatController chatController = null;

	// Constructor
	public UserController() {
		// Initialize the user controller
		initialize();
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	private void initialize() {
		// Initialize the user controller
		this.isLoggedIn = false;
		chatController = new ChatController();
	}

	public ChatController getChatController() {
		return chatController;
	}

	public JSONObject login(String username, String password) {
		if (isLoggedIn) {
			System.out.println("Already logged in as " + currentUser.getUsername());
			return null;
		}

		// Send a POST request to the server with username and password
		JSONObject response = null;

		try {
			// Send a POST request to the server with username and password
			response = FetchUtils.post(
					API + "/login",
					Map.of("username", username, "password", password),
					null, // No access token
					null  // No refresh token
			);

			// Check if the response is successful
			if (response.getInt("statusCode") == 200) {
				// Extract tokens from response headers (not required here, for example, handle separately)
				String accessToken = response.getString("accessToken");
				String refreshToken = response.getString("refreshToken");

				// Save tokens
				if (accessToken != null && refreshToken != null) {
					TokenUtils.saveTokens(accessToken, refreshToken);
				}

				// Set user as logged in
				this.isLoggedIn = true;
				currentUser = new User(username);
			}
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public JSONObject register(String username, String password) {
		if (isLoggedIn) {
			System.out.println("Already logged in as " + currentUser.getUsername());
			return null;
		}

		// Send a POST request to the server with username and password
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/register",
					Map.of("username", username, "password", password),
					null, // No access token
					null  // No refresh token
			);

			if (response.getInt("statusCode") == 200) {
				// Extract tokens from response headers (not required here, for example, handle separately)
				String accessToken = response.getString("accessToken");
				String refreshToken = response.getString("refreshToken");

				// Set user as logged in
				if (accessToken != null && refreshToken != null) {
					TokenUtils.saveTokens(accessToken, refreshToken);
				}

				// Set user as logged in
				this.isLoggedIn = true;
				currentUser = new User(username);
			}
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public void logout() {
		try {
			TokenUtils.clearTokens();
			currentUser.cleanup();
			this.isLoggedIn = false;
			currentUser = null;

			// Switch window
			SceneController.switchStage(ScreenName.Home);
		} catch (Exception error) {
			System.out.println("Failed to logout");
		}
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}
}
