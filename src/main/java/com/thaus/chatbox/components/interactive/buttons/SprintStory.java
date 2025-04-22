package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Member;
import com.thaus.chatbox.classes.Story;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SprintStory extends HBox implements ICustomNode {

	@FXML
	private Label nameLabel;
	@FXML
	private Label userLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private JFXButton doneButton;
	@FXML
	private Label dateLabel;

	//
	private String storyId;
	private Runnable onStartClickHandler;
	private Runnable onStopClickHandler;
	private ChangeListener<Object> dateListener;

	public SprintStory(Story story) {
		initializeFXML("/components/interactive/sprint-story.fxml");
		System.out.println("Initializing SprintStory");
		storyId = story.getId();
		initialize(story);
	}

	public void initialize(Story story) {
		// Initialize the button with the name and id
		nameLabel.textProperty().bind(story.getName());

		dateListener = (observable, oldValue, newValue) -> {
			updateDateLabel(story);
		};

		// Date listener
		story.getStartedAt().addListener(dateListener);
		story.getEndAt().addListener(dateListener);
		updateDateLabel(story);

		// Remove listeners when component is removed from scene - only cleanup when removed
		this.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene == null) {
				System.out.println("StoryButton removed from scene, cleaning up");
			}
		});
	}

	public void updateDateLabel(Story story) {
		if (story.getStartedAt().get() == null && story.getEndAt().get() == null) {
			dateLabel.setText("");
			statusLabel.setText("Not started");
			statusLabel.getStyleClass().add("red");
			doneButton.setText("Start now");
			doneButton.setOnAction(event -> {
				if (onStartClickHandler != null) {
					onStartClickHandler.run();
				}
			});
		} else {
			StringBuilder dateInfo = new StringBuilder();

			if (story.getStartedAt().get() != null) {
				dateInfo.append("Started at: ").append(story.getStartedAt().get());
				doneButton.setText("End now");
				doneButton.setOnAction(event -> {
					if (onStopClickHandler != null) {
						onStopClickHandler.run();
					}
				});
			}

			if (story.getEndAt().get() != null) {
				if (dateInfo.length() > 0) {
					dateInfo.append(" and ");
				}
				dateInfo.append("Ended at: ").append(story.getEndAt().get());
				statusLabel.setText("Finished");
				statusLabel.getStyleClass().remove("red");
				statusLabel.getStyleClass().add("green");

				doneButton.setVisible(false);
				doneButton.setManaged(false);
				doneButton.setOnAction(null);
			}
			dateLabel.setText(dateInfo.toString());
		}

		// Get userId name
		ObservableList<Member> members = UserController.getChatController().currentGroup().getMembers();
		String username = members.stream()
				.filter(member -> member.getId().equals(story.getUserId().get()))
				.map((member) -> member.getUsername().get())
				.findFirst()
				.orElse(null);

		if (userLabel != null && username != null) {
			userLabel.setVisible(true);
			userLabel.setManaged(true);
			userLabel.setText(username);
		} else {
			userLabel.setVisible(false);
			userLabel.setManaged(false);
		}
	}
	
	public String getStoryId() {
		return this.storyId;
	}

	public void handleStartClick(Runnable onClickHandler) {
		this.onStartClickHandler = onClickHandler;
	}

	public void handleStopClick(Runnable onClickHandler) {
		this.onStopClickHandler = onClickHandler;
	}

	public void clear(Story story) {
		// Clear the component
		nameLabel.textProperty().unbind();
		story.getStartedAt().removeListener(dateListener);
		story.getEndAt().removeListener(dateListener);
	}
}
