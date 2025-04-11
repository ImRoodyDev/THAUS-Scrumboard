package com.thaus.chatbox.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.Sidebar;
import javafx.fxml.FXML;

public class HomeController extends BaseScene {
	@FXML
	private Sidebar sidebar;

	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();

		// sidebar.initialize();
		System.out.println("Initialize FXML controller's");
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onOpen() {

	}
}
