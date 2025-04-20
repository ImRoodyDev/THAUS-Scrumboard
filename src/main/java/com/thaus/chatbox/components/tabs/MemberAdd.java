package com.thaus.chatbox.components.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.thaus.chatbox.interfaces.ICustomNode;
import com.thaus.chatbox.types.MemberType;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MemberAdd extends VBox implements ICustomNode {

	// Components
	@FXML
	private JFXComboBox<String> memberCombo;
	@FXML
	private TextField usernameField;
	@FXML
	private JFXButton addBt;
	@FXML
	private JFXButton cancelBt;

	// Component controllers
	private MemberType memberType;
	private OnActionSubmitHandler onActionSubmit;
	private Runnable onCancelAction;

	public MemberAdd() {
		initializeFXML("/components/tabs/add-member.fxml");
	}

	@FXML
	public void initialize() {
		// Initialize combo box
		MemberType[] types = MemberType.values();

		if (memberCombo != null) {
			for (MemberType type : types) {
				memberCombo.getItems().add(type.getName());
			}

			// Set default selection to TEAM
			memberCombo.getSelectionModel().select(MemberType.MEMBER.getName());

			memberCombo.setOnAction(_ -> {
				// String selectedItem = memberCombo.getSelectionModel().getSelectedItem();
				MemberType type = MemberType.fromName(memberCombo.getSelectionModel().getSelectedItem());
				memberType = type;
			});
		}


	}

	// Set on submit
	public void setOnActionSubmit(OnActionSubmitHandler handler) {
		this.onActionSubmit = handler;
		addBt.setOnAction(_ -> {
					onActionSubmit.handle(memberType, usernameField.getText());
				}
		);
	}

	// Set on cancel
	public void setOnCancelAction(Runnable cancelHandler) {
		this.onCancelAction = cancelHandler;
		cancelBt.setOnAction(_ -> onCancelAction.run());
	}

	//  Create Chat handler
	public interface OnActionSubmitHandler {
		void handle(MemberType type, String name);
	}
}
