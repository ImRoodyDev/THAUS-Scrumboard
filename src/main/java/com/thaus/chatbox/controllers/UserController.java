package com.thaus.chatbox.controllers;

import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.User;
import com.thaus.chatbox.types.GroupType;
import com.thaus.chatbox.types.MemberType;
import com.thaus.chatbox.types.ScreenName;
import com.thaus.chatbox.utils.DateUtils;
import com.thaus.chatbox.utils.FetchUtils;
import com.thaus.chatbox.utils.TokenUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class UserController {
	private static final String API = "http://localhost:3004/api";
	private static boolean isLoggedIn = false;
	private static User currentUser = null;
	private static GroupController groupController = null;

	// UserController variables
	private static String accessToken = null;
	private static String refreshToken = null;


	// Constructor
	public UserController() {
		// Initialize the user controller
		initialize();
	}

	private void initialize() {
		// Load user tokens
		String[] tokens = TokenUtils.loadTokens();

		if (tokens != null) {
			accessToken = tokens[0];
			refreshToken = tokens[1];
		}

		// Initialize the user controller
		isLoggedIn = false;

		// Check if the user is logged in
		if (accessToken != null && refreshToken != null) {
			JSONObject response = getUser();
			if (response.getInt("statusCode") <= 203) {
				createUser(response);
				isLoggedIn = true;
			} else {
				TokenUtils.clearTokens();
			}
		}
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static GroupController getChatController() {
		return groupController;
	}


	// ### Authentication Endpoints ###

	public static JSONObject getUser() {
		// Send a GET request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.get(
					API + "/user",
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}
		return response;
	}

	public static JSONObject login(String username, String password) {
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
			if (response.getInt("statusCode") <= 203) {
				// Extract tokens from response headers (not required here, for example, handle separately)
				accessToken = response.getString("accessToken");
				refreshToken = response.getString("refreshToken");

				// Save tokens
				if (accessToken != null && refreshToken != null) {
					TokenUtils.saveTokens(accessToken, refreshToken);
				}

				// Set user as logged in
				isLoggedIn = true;
				createUser(response);
			}
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
			error.printStackTrace();
		}

		return response;
	}

	public static JSONObject register(String username, String password) {
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

			if (response.getInt("statusCode") <= 203) {
				// Extract tokens from response headers (not required here, for example, handle separately)
				accessToken = response.getString("accessToken");
				refreshToken = response.getString("refreshToken");

				// Set user as logged in
				if (accessToken != null && refreshToken != null) {
					TokenUtils.saveTokens(accessToken, refreshToken);
				}

				// Set user as logged in
				isLoggedIn = true;
				createUser(response);
			}
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
			error.printStackTrace();
		}

		return response;
	}

	public static boolean isLoggedIn() {
		return isLoggedIn;
	}

	public static void logout() {
		try {
			TokenUtils.clearTokens();
			currentUser.cleanup();
			isLoggedIn = false;
			currentUser = null;

			// Switch window
			SceneController.switchStage(ScreenName.Home);
		} catch (Exception error) {
			System.out.println("Failed to logout");
		}
	}

	// ### Group Endpoints ###

	public static JSONObject getGroup(String groupId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a GET request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.get(
					API + "/group/get-group/" + groupId,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject createGroup(String name, GroupType type) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/group/create-group",
					Map.of("groupName", name, "groupType", type.getName()),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject deleteGroup(String groupId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.get(
					API + "/group/delete-group/" + groupId,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}


	// Creating a new user in the application on initialize and on login or register
	public static void createUser(JSONObject userData) {
		JSONObject user = userData.getJSONObject("user");
		JSONArray groups = userData.getJSONArray("groups");

		// Extract user information
		String userId = user.getString("id");
		String username = user.getString("username");
		currentUser = new User(userId, username);

		// Iterate through the groups
		if (groups != null)
			for (int i = 0; i < groups.length(); i++) {
				JSONObject group = groups.getJSONObject(i);

				// Extract group information
				String groupId = group.getString("id");
				String groupType = group.getString("type");
				String groupName = group.getString("name");
				String groupCreatedAt = group.getString("createdAt");
				String role = group.getString("role");
				boolean isOwner = role.equals("admin");

				GroupType type = GroupType.fromName(groupType);

				// Create a new group object
				Group groupObj = new Group(
						groupId,
						groupName,
						isOwner,
						DateUtils.parseAndFormatDate(groupCreatedAt),
						type);
				currentUser.addGroup(groupObj);
			}

		// Create a new chat controller
		groupController = new GroupController();
	}

	public static JSONObject addMember(String groupId, String memberUsername, MemberType memberType) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&username=%s&role=%s", groupId, memberUsername, memberType.getName());
			response = FetchUtils.get(
					API + "/group/add-member" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
			// error.printStackTrace();
		}

		return response;
	}

	public static JSONObject removeMember(String groupId, String memberId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&username=%s", groupId, memberId);
			response = FetchUtils.get(
					API + "/group/remove-member" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject createFeature(String groupId, String name) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/feature/add-feature",
					Map.of("groupId", groupId, "name", name),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject deleteFeature(String groupId, String id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&featureId=%s", groupId, id);
			response = FetchUtils.get(
					API + "/feature/delete-feature" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject createEpic(String groupId, String featureId, String name) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/epic/add-epic",
					Map.of("groupId", groupId, "featureId", featureId, "name", name),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject deleteEpic(String groupId, String featureId, String epicId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&featureId=%s&epicId=%s", groupId, featureId, epicId);
			response = FetchUtils.get(
					API + "/epic/delete-epic" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject createStory(String groupId, String featureId, String epicId, String name, String description) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/story/add-story",
					Map.of("groupId", groupId, "featureId", featureId, "epicId", epicId, "name", name, "description", description),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject deleteStory(String groupId, String featureId, String epicId, String userStoryId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&featureId=%s&epicId=%s&userStoryId=%s", groupId, featureId, epicId, userStoryId);
			response = FetchUtils.get(
					API + "/story/delete-story" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject addSprint(String groupId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/sprint/add-sprint",
					Map.of("groupId", groupId),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject deleteSprint(String groupId, String sprintId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&sprintId=%s", groupId, sprintId);
			response = FetchUtils.get(
					API + "/sprint/delete-sprint" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject startSprint(String groupId, String sprintId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/sprint/start-sprint",
					Map.of("groupId", groupId, "sprintId", sprintId),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject linkStoryToSprint(String groupId, String sprintId, String storyId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/sprint/link-storyW",
					Map.of("groupId", groupId, "sprintId", sprintId, "storyId", storyId),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject unlinkStoryFromSprint(String groupId, String sprintId, String storyId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&sprintId=%s&storyId=%s", groupId, sprintId, storyId);
			response = FetchUtils.get(
					API + "/sprint/unlink-story",
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject updateStoryStatus(String groupId, String storyId, boolean isEnded) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/sprint/story-status",
					Map.of(
							"groupId", groupId,
							"storyId", storyId,
							"end", String.valueOf(isEnded),
							"start", String.valueOf(!isEnded)
					), accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject getSprintMessages(String groupId, String sprintId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&sprintId=%s", groupId, sprintId);
			response = FetchUtils.get(
					API + "/chat/get-messages/sprint" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject getGroupMessages(String groupId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s", groupId);
			response = FetchUtils.get(
					API + "/chat/get-messages/group" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject getStoryMessages(String groupId, String storyId) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			String query = String.format("?groupId=%s&storyId=%s", groupId, storyId);
			response = FetchUtils.get(
					API + "/chat/get-messages/story" + query,
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject sendMessage(String groupId, String message) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/chat/add-message/group",
					Map.of("groupId", groupId, "message", message),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject sendSprintMessage(String groupId, String sprintId, String message) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/chat/add-message/sprint",
					Map.of("groupId", groupId, "sprintId", sprintId, "message", message),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}

		return response;
	}

	public static JSONObject sendStoryMessage(String groupId, String storyId, String message) {
		if (!isLoggedIn) {
			System.out.println("Not logged in");
			return null;
		}

		// Send a POST request to the server with group name and type
		JSONObject response = null;

		try {
			response = FetchUtils.post(
					API + "/chat/add-message/story",
					Map.of("groupId", groupId, "storyId", storyId, "message", message),
					accessToken,
					refreshToken);
		} catch (Exception error) {
			System.out.println("Fetch error: " + error.getMessage());
		}
		return response;
	}
}
