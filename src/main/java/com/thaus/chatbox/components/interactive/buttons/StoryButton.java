package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Sprint;
import com.thaus.chatbox.classes.Story;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.utils.Task;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class StoryButton extends HBox implements ICustomNode {

	@FXML
	private Label nameLabel;
	@FXML
	private Label unreadLabel;
	@FXML
	private JFXButton button;
	@FXML
	private JFXButton contextMenuButton;
	@FXML
	private ContextMenu contextMenu;
	@FXML
	private Label sprintLabel;

	private Menu sprintMenu;

	//
	private String storyId;
	private OnSprintClickHandler onSprintClickHandler;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;
	private ChangeListener<Object> sprintIdListener;

	public StoryButton(Story story) {
		initializeFXML("/components/interactive/user-story.fxml");
		this.storyId = story.getId();
		initialize(story);
	}

	public void initialize(Story story) {
		// Initialize the button with the name and id
		nameLabel.textProperty().bind(story.getName());

		// Set id listener - fixed to correctly handle the value change
		sprintIdListener = (observable, oldValue, newValue) -> {
			System.out.println("Sprint ID changed: old=" + oldValue + ", new=" + newValue);
			initializeMenu(story);
		};
		story.getSprintId().addListener(sprintIdListener);

		// Set up the button actions
		button.setOnAction(_ -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});

		initializeMenu(story);

		// Remove listeners when component is removed from scene - only cleanup when removed
		this.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene == null) {
				System.out.println("StoryButton removed from scene, cleaning up");
				story.getSprintId().removeListener(sprintIdListener);
				sprintIdListener = null;
				cleanup();
			}
		});
	}

	private void initializeMenu(Story story) {
		// Initialize the context menu
		if (contextMenuButton != null) {
			contextMenu.getItems().clear();

			if (story.getSprintId().get() == null) {
				ObservableList<Sprint> sprints = UserController.getChatController().currentGroup().getSprints();
				sprintMenu = new Menu("Add to Sprint");

				Task.runUITask(() -> {
					// Initialize the sprint menu
					for (int i = 0; i < sprints.size(); i++) {
						Sprint sprint = sprints.get(i);
						if (sprint.getStartedAt().get() != null || sprint.getEndAt().get() != null) {
							continue; // Skip if the sprint is in progress
						}

						MenuItem menuItem = new MenuItem();
						menuItem.textProperty().bind(sprint.getName());
						sprintMenu.getItems().add(menuItem);

						// Set up the menu item action
						menuItem.setOnAction(_ -> {
							Task.runTask(() -> {
								if (onSprintClickHandler != null) {
									onSprintClickHandler.onUserStoryClick(sprint);
								}
							});
						});
					}

					// Disable sprint label when no sprint is assigned
					sprintLabel.setVisible(false);
					sprintLabel.setManaged(false);
				});
			} else {
				Sprint sprint = UserController.getChatController().currentGroup().getSprintById(story.getSprintId().get());

				// Important: Configure the sprint label when a sprint is assigned
				Task.runUITask(() -> {
					// Set the sprint label to the current sprint
					if (sprint != null) {
						sprintLabel.setVisible(true);
						sprintLabel.setManaged(true);
						sprintLabel.textProperty().bind(sprint.getName());
					} else {
						sprintLabel.setVisible(false);
						sprintLabel.setManaged(false);
					}
				});
			}

			MenuItem deleteItem = new MenuItem("Delete");

			// Create menu action
			deleteItem.setOnAction(e -> {
				if (onDeleteHandler != null) {
					onDeleteHandler.run();
				}
			});
			contextMenuButton.setOnAction(event -> {
				if (contextMenu != null) {
					contextMenu.show(contextMenuButton, Side.BOTTOM, 0, 0);
				}
			});

			// Add items to the context menu
			if (story.getSprintId().get() == null) {
				contextMenu.getItems().addAll(sprintMenu, deleteItem);
			} else {
				contextMenu.getItems().add(deleteItem);
			}
		}
	}

	public String getStoryId() {
		return storyId;
	}

	public void handleOnSprintClick(OnSprintClickHandler onSprintClickHandler) {
		this.onSprintClickHandler = onSprintClickHandler;
	}

	public void handleOnDelete(Runnable onDeleteHandler) {
		this.onDeleteHandler = onDeleteHandler;
	}

	public void handleOnClick(Runnable onClickHandler) {
		this.onClickHandler = onClickHandler;
	}

	private void cleanup() {
		// System.out.println("Cleaning up story button for: " + (story != null ? story.getId() : "null"));

		/*if (story != null && sprintIdListener != null) {
			System.out.println("Removing sprintIdListener");
			story.getSprintId().removeListener(sprintIdListener);
			sprintIdListener = null;
			// Clear references
			// story = null;
		}*/

		// Unbind each child in the sprint menu
		if (sprintMenu != null && sprintMenu.getItems() != null) {
			for (MenuItem menuItem : sprintMenu.getItems()) {
				menuItem.textProperty().unbind();
			}
		}

		// Unbind the nameLabel text property to prevent memory leaks
		if (nameLabel != null) {
			nameLabel.textProperty().unbind();
		}

		if (sprintLabel != null) {
			sprintLabel.textProperty().unbind();
		}

	}

	public interface OnSprintClickHandler {
		void onUserStoryClick(Sprint sprint);
	}
}
