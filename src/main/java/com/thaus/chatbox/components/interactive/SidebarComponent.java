package com.thaus.chatbox.components.interactive;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.components.interactive.buttons.GroupButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.utils.Task;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class SidebarComponent extends AnchorPane implements ICustomNode {
	// Toggle searchbar element
	private boolean isSearchOpen = false;
	// Search bar controller
	@FXML
	private SearchBarComponent searchBar;
	// Sidebar components
	@FXML
	private FlowPane filtersFlowPane;
	@FXML
	private VBox separatorBar;
	@FXML
	private JFXButton filterBtn;
	@FXML
	private JFXButton searchBtn;
	@FXML
	private VBox chatboxsScrollContent;
	@FXML
	private JFXButton newChatboxBtn;

	// Initialize listeners
	private ListChangeListener<Group> groupListChangeListener;
	private HandleGroupClick groupClickListener;
	private HandleGroupDelete groupDeleteListener;

	// Default constructor needed for JavaFX
	public SidebarComponent() {
		initializeFXML("/components/tabs/sidebar.fxml");
	}

	@FXML
	public void initialize() {
		// Initialize buttons
		initializeButtons();

		// Initialize Chatbox
		initializeChatboxs();
		initializeListeners();
	}

	// Initialize Filter controller
	private void initializeListeners() {
		initializeSearchBar();
		initializeFilters();

		// Initialize group list change listener
		groupListChangeListener = change -> {
			while (change.next()) {
				if (change.wasAdded()) {
					for (Group group : change.getAddedSubList()) {
						GroupButton groupButton = createGroupButton(group);

						// Handle click action on chatbox
						groupButton.onClickHandle(() -> {
							if (groupClickListener != null) {
								groupClickListener.onGroupClick(group);
							}
						});

						// Handle delete action on chatbox
						groupButton.onDeleteAction(() -> {
							if (groupDeleteListener != null) {
								groupDeleteListener.onGroupDelete(group);
							}
						});
					}
				}

				if (change.wasRemoved()) {
					for (Group group : change.getRemoved()) {
						removeChatbox(group); // Ensure this method removes the group UI
					}
				}
			}
		};

		UserController.getCurrentUser().getGroups().addListener(groupListChangeListener);

		// Add current buttons from user groups
		UserController.getCurrentUser().getGroups().forEach(
				group -> {
					GroupButton groupButton = createGroupButton(group);

					// Handle click action on chatbox
					groupButton.onClickHandle(() -> {
						if (groupClickListener != null) {
							groupClickListener.onGroupClick(group);
						}
					});

					// Handle delete action on chatbox
					groupButton.onDeleteAction(() -> {
						if (groupDeleteListener != null) {
							groupDeleteListener.onGroupDelete(group);
						}
					});
				}
		);
	}

	// Initialize chatboxs
	private GroupButton createGroupButton(Group group) {
		GroupButton groupButton = new GroupButton(group);
		groupButton.onClickHandle(() -> {
			if (groupClickListener != null) {
				groupClickListener.onGroupClick(group);
			}
		});
		groupButton.onDeleteAction(() -> {
			if (groupDeleteListener != null) {
				groupDeleteListener.onGroupDelete(group);
			}
		});

		// Add the new group button
		Task.runUITask(() ->
				chatboxsScrollContent.getChildren().add(groupButton)
		);

		return groupButton;
	}

	// Remove chatbox
	private void removeChatbox(Group group) {
		Task.runUITask(() -> chatboxsScrollContent.getChildren().removeIf(node ->
				node instanceof GroupButton && ((GroupButton) node).getGroupId().equals(group.getId())
		));
	}


	// Initialize filter bar
	private void initializeFilters() {
		// Initialize filter controller
		filtersFlowPane.getChildren().clear();
	}

	// Initialize searchbar
	private void initializeSearchBar() {
		searchBar = new SearchBarComponent(isSearchOpen, new ISearchListeners() {
			@Override
			public void onSubmit(String text) {
				System.out.println("Search submitted: " + text);
			}

			@Override
			public void onTextChanged(String text) {
				System.out.println("Search text changed: " + text);
			}
		});

		// Add element in the separator bar
		if (separatorBar != null) {
			separatorBar.getChildren().add(searchBar);
		} else {
			System.out.println("(SideBar) separatorBar is null");
		}
	}

	// Initialize buttons
	private void initializeButtons() {
		// Initialize filter button
		filterBtn.setVisible(false);
		filterBtn.setManaged(false);


		// Initialize search button
		searchBtn.setOnAction(_ -> toggleSearch());
	}

	// Initialize chatbox
	private void initializeChatboxs() {
		// Clear chatbox content remove template
		chatboxsScrollContent.getChildren().clear();
	}

	// Toggle search bar
	private void toggleSearch() {
		isSearchOpen = !isSearchOpen;
		searchBar.enableComponent(isSearchOpen);
	}

	// Set action on create chat
	public void handleCreateChat(Runnable action) {
		newChatboxBtn.setOnAction(event -> action.run());
	}

	// Set action on group click
	public void handleGroupClick(HandleGroupClick action) {
		this.groupClickListener = action;
	}

	// Set action on group delete
	public void handleGroupDelete(HandleGroupDelete action) {
		this.groupDeleteListener = action;
	}

	public void cleanup() {
		// Remove listeners
		UserController.getCurrentUser().getGroups().removeListener(groupListChangeListener);
	}

	public interface HandleGroupClick {
		void onGroupClick(Group group);
	}

	public interface HandleGroupDelete {
		void onGroupDelete(Group group);
	}

}
