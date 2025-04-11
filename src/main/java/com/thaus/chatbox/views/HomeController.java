package com.thaus.chatbox.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.Sidebar;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.types.ChatboxType;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class HomeController extends BaseScene {
	// Controllers
	private Sidebar sidebar;

	// Components inside the home fxml
	@FXML
	public BorderPane borderPane;


	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();
		initializeSidebar();
		System.out.println("Initialize FXML controller's");
	}
	@Override
	public void onClose() {

	}
	@Override
	public void onOpen() {

	}


	// Initialize sidebar component
	private void initializeSidebar(){
		// Create Sidebar component
		sidebar = new Sidebar(
				new IChatboxFilterListener() {
					@Override
					public void onFilterApplied(ChatboxType type) {

					}

					@Override
					public void onFilterRemoved(ChatboxType type) {

					}

					@Override
					public void onAllFiltersRemoved() {

					}
				},
				new ISearchListeners() {
					@Override
					public void onSubmit(String text) {

					}

					@Override
					public void onTextChanged(String text) {

					}
				}

		);

		// Append in viewport
		borderPane.setLeft(sidebar);
	}
}
