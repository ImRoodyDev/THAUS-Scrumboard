package com.thaus.chatbox.components.interactive.buttons;

import com.jfoenix.controls.JFXButton;
import com.thaus.chatbox.classes.Member;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.MemberType;
import com.thaus.chatbox.utils.ColorUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MemberButton extends HBox implements ICustomNode {
	// FXML components
	@FXML
	private Label nameLabel;
	@FXML
	private Label profileLabel;
	@FXML
	private Label memberLabel;
	@FXML
	private VBox profileBackground;
	@FXML
	private Label dateLabel;
	@FXML
	private JFXButton button;
	@FXML
	private JFXButton deleteButton;

	// Observable properties
	private final String memberId;
	private Runnable onClickHandler;
	private Runnable onDeleteHandler;

	public MemberButton(Member member) {
		this.memberId = member.getId();
		initializeFXML("/components/interactive/member.fxml");
		initialize(member);
	}

	public void initialize(Member member) {
		initializeLabels(member);

		button.setOnAction(event -> {
			if (onClickHandler != null) {
				onClickHandler.run();
			}
		});

		deleteButton.setOnAction(event -> {
			if (onDeleteHandler != null) {
				onDeleteHandler.run();
			}
		});
	}

	private void initializeLabels(Member member) {
		nameLabel.textProperty().bind(member.getUsername());
		memberLabel.setText(member.getRole().get().getName());
		profileLabel.textProperty().bind(
				Bindings.createStringBinding(
						() -> {
							String username = member.getUsername().getValue();
							return (username != null && !username.isEmpty()) ?
									username.substring(0, 1).toUpperCase() : "";
						},
						member.getUsername()
				)
		);
		dateLabel.setText(
				"Joined at: " + member.getJoinedAt()
		);

		// Random color for the profile background skip black
		profileBackground.setStyle(
				profileBackground.getStyle() + " -fx-background-color: " + ColorUtils.getRandomColorExceptBlack()
		);

		if (!UserController.getChatController().currentGroup().isOwner()) {
			deleteButton.setVisible(false);
			deleteButton.setManaged(false);
		}

		if (member.getRole().get() == MemberType.SCRUM_MASTER) {
			deleteButton.setVisible(false);
		}
	}

	public String getMemberId() {
		return memberId;
	}

	public void handleMemberClick(Runnable handler) {
		this.onClickHandler = handler;
	}

	public void handleMemberDelete(Runnable handler) {
		this.onDeleteHandler = handler;
	}
}
