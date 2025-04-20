package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.thaus.chatbox.classes.Feature;
import com.thaus.chatbox.classes.Group;
import com.thaus.chatbox.classes.Member;
import com.thaus.chatbox.components.interactive.buttons.MemberButton;
import com.thaus.chatbox.controllers.UserController;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.MemberType;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.util.Date;

public class GroupMembers extends VBox implements ICustomNode {
	// FXML components // Text field for creating new feature
	@FXML
	private TextField textField;
	@FXML
	private JFXButton createButton;
	@FXML
	private JFXComboBox<String> memberCombo;
	@FXML
	private VBox viewContainer;
	@FXML
	private VBox dialog;
	@FXML
	private Label dialogLabel;

	// Observable properties
	private String groupId;
	private MemberType memberType;
	private onMemberClickHandler onMemberClickHandler;
	private ListChangeListener<Member> memberListChangeListener = change -> {
		while (change.next()) {

			if (change.wasAdded()) {
				for (Member member : change.getAddedSubList()) {
					createMemberComponent(member);
				}
			}

			if (change.wasRemoved()) {
				for (Member member : change.getRemoved()) {
					// Remove the member from the view container
					viewContainer.getChildren().removeIf(node -> {
						if (node instanceof MemberButton memberButton) {
							return memberButton.getMemberId().equals(member.getId());
						}
						return false;
					});
				}
			}
		}
	};

	// Constructor
	public GroupMembers() {
		initializeFXML("/components/tabs/group-members.fxml");
	}

	@FXML
	public void initialize() {
		Group group = UserController.getChatController().currentGroup();
		this.groupId = group.getId();
		initializeMembers(group);
		initializeButtons();
	}

	public void initializeButtons() {
		if (memberCombo != null) {
			MemberType[] types = MemberType.values();
			for (MemberType type : types) {
				memberCombo.getItems().add(type.getName());
			}

			memberCombo.setOnAction(_ -> {
				String selectedItem = memberCombo.getSelectionModel().getSelectedItem();
				memberType = MemberType.fromName(memberCombo.getSelectionModel().getSelectedItem());
			});

			// Set default selection to TEAM
			memberCombo.getSelectionModel().select(MemberType.MEMBER.getName());
			memberType = MemberType.MEMBER;
		}

		// Disable dialog
		dialog.setVisible(false);
		dialog.setManaged(false);
	}

	// Initialize the member list
	private void initializeMembers(Group currentGroup) {

		// Add the listener to the features list
		currentGroup.getMembers().addListener(memberListChangeListener);

		// Add each feature to the view container
		for (Member member : currentGroup.getMembers()) {
			createMemberComponent(member);
		}

		// Add the create button action
		createButton.setOnAction(_ -> addMember());
	}

	private void addMember() {
		// Check if the text field is empty
		if (textField.getText().isEmpty()) {
			return;
		}
		
		// Create a new feature
		JSONObject response = UserController.addMember(
				groupId,
				textField.getText(),
				memberType
		);

		if (response == null || response.getInt("statusCode") > 203) {
			String message = response.getString("message");
			showToast(message);
		} else {
			JSONObject member = response.getJSONObject("member");
			String memberId = member.getString("id");
			String memberName = member.getString("username");
			Group currentGroup = UserController.getChatController().currentGroup();

			currentGroup.addMember(new Member(
					memberId,
					memberName,
					new Date(),
					memberType.getName()
			));

			// Clear the text field
			textField.clear();
		}
	}

	private void removeMember(Member member) {
		// Check if the member is null
		if (member == null) {
			return;
		}

		// Delete the member
		JSONObject response = UserController.removeMember(groupId, member.getId());

		if (response == null || response.getInt("statusCode") > 203) {
			String message = response.getString("message");
			showToast(message);
		} else {
			UserController.getChatController().currentGroup().removeMember(member);
		}
	}

	private void createMemberComponent(Member member) {
		// Create a new member button
		MemberButton memberButton = new MemberButton(member);
		memberButton.handleMemberDelete(() -> removeMember(member));

		// Add it to the view container
		viewContainer.getChildren().add(memberButton);
	}

	private void showToast(String message) {
		System.out.println(message);
		dialogLabel.setText(message);
		dialog.setVisible(true);
		dialog.setManaged(true);

		// Hide the dialog after 3 seconds
		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			dialog.setVisible(false);
			dialog.setManaged(false);
		}).start();
	}

	public void cleanup() {
		// Remove the listener
		onMemberClickHandler = null;
		createButton.setOnAction(null);
		viewContainer.getChildren().clear();
		UserController.getChatController().currentGroup().getMembers().removeListener(memberListChangeListener);
	}

	public interface onMemberClickHandler {
		void onFeatureClick(Feature feature);
	}
}
