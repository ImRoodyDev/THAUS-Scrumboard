package com.thaus.chatbox.components.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.components.interactive.SidebarComponent;
import com.thaus.chatbox.components.tabs.GroupCreate;
import com.thaus.chatbox.components.tabs.WelcomeComponent;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.types.GroupType;
import com.thaus.chatbox.utils.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.json.JSONObject;

import java.util.Date;

public class HomeController extends BaseScene {
	// Components inside the home fxml
	@FXML
	private BorderPane borderPane;    // Chat change listener
	@FXML
	private AnchorPane viewportContainer;
	// Custom Components
	private SidebarComponent sidebar;


	// Page values
	private HomeTab currentTab;

	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();
		initializeSidebar();
		System.out.println("Initialize Home controller's");

		// Going to the current page
		switchTab(HomeTab.WELCOME);
	}

	@Override
	public void onOpen() {

	}

	@Override
	public void onClose() {
		sidebar.cleanup();
	}

	// Initialize sidebar component
	private void initializeSidebar() {
		// Create Sidebar component listeners
		sidebar = new SidebarComponent();

		// Sidebar actions handlers
		sidebar.handleCreateChat(() -> switchTab(HomeTab.CREATE_GROUP));
		sidebar.handleGroupClick((Group group) -> {
			getGroupController().selectGroup(group,
					() -> {
						if (currentTab != HomeTab.CHAT) {
							switchTab(HomeTab.CHAT);
						}
					});
		});
		sidebar.handleGroupDelete((Group group) -> {
			deleteGroup(group);
			if (getGroupController().currentGroup() == group) {
				switchTab(HomeTab.WELCOME);
				getGroupController().deselectCurrentGroup();
			}
		});

		// Append in viewport
		borderPane.setLeft(sidebar);
	}

	// Switch to tab in the home page
	private void switchTab(HomeTab tabName) {
		if (currentTab == tabName) {
			System.out.println("You are already at tab: " + tabName);
			return;
		}

		try {
			// Loader for FXML
			Node newContent = null;

			switch (tabName) {
				case WELCOME:
					newContent = new WelcomeComponent();
					break;
				case CREATE_GROUP:
					GroupCreate createChatMenu = new GroupCreate();

					// On cancel menu
					createChatMenu.setOnCancelAction(() -> switchTab(HomeTab.WELCOME));

					// Cn submit to create a new chatbox
					createChatMenu.setOnActionSubmit((GroupType type, String name) -> {
						Task.runTask(() -> {
							Group createdGroup = createGroup(name, type);
							if (createdGroup == null) {
								return;
							}
							getGroupController().selectGroup(createdGroup);
							Task.runUITask(() -> switchTab(HomeTab.CHAT));
						});
					});

					// Add it to the temporary content
					newContent = createChatMenu;
					break;
				case CHAT:
					ChatWindowController chatComponent = new ChatWindowController();
					newContent = chatComponent;
					break;
			}

			if (newContent != null) {
				// Clear contents and add new
				viewportContainer.getChildren().clear();
				viewportContainer.getChildren().add(newContent);

				// Set anchor position
				AnchorPane.setTopAnchor(newContent, 0.0);
				AnchorPane.setBottomAnchor(newContent, 0.0);
				AnchorPane.setLeftAnchor(newContent, 0.0);
				AnchorPane.setRightAnchor(newContent, 0.0);
				currentTab = tabName;
				System.out.println("Switching tab: " + tabName);
			}
		} catch (Exception e) {
			System.err.println("Error loading tab: " + tabName);
			e.printStackTrace();
		}
	}

	private Group createGroup(String name, GroupType type) {
		JSONObject response = UserController.createGroup(name, type);
		Group newGroup = null;

		if (response.getInt("statusCode") > 203) {
			String message = response.getString("message");
			showError(message);
		} else {
			JSONObject group = response.getJSONObject("group");
			String groupId = group.getString("id");
			newGroup = new Group(
					groupId,
					name,
					true,
					new Date(),
					type
			);
			getUser().addGroup(newGroup);
		}
		return newGroup;
	}

	private void deleteGroup(Group group) {
		JSONObject response = UserController.deleteGroup(group.getId());

		if (response.getInt("statusCode") > 203) {
			String message = response.getString("message");
			showError(message);
		} else {
			getUser().removeGroup(group);
		}
	}

	// Type of Tabs in home page
	public enum HomeTab {
		WELCOME,
		CREATE_GROUP,
		CHAT,

	}
}
