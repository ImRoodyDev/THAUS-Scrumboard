package com.thaus.chatbox.views;

import com.thaus.chatbox.base.BaseScene;
import com.thaus.chatbox.components.ChatboxButton;
import com.thaus.chatbox.components.Sidebar;
import com.thaus.chatbox.controllers.ChatController;
import com.thaus.chatbox.interfaces.IChatListener;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.interfaces.ISearchListeners;
import com.thaus.chatbox.types.ChatboxType;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class HomeController extends BaseScene {
	// Components inside the home fxml
	@FXML
	public BorderPane borderPane;
	// Controllers
	private Sidebar sidebar;
	private ChatController chatController;

	// This method will be automatically called after the FXML is loaded
	@Override
	public void initialize() {
		super.initialize();
		initializeSidebar();
		initializeChatController();
		System.out.println("Initialize Home controller's");
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onOpen() {

	}


	// Initialize sidebar component
	private void initializeSidebar() {
		// Create Sidebar component listeners
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

	// Initialize chat controller
	private void initializeChatController() {
		// Create the chat controller listener
		IChatListener chatListener = new IChatListener() {

			@Override
			public void initializeChatboxs(ArrayList<ChatboxButton> chatboxButtons) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().addAll(chatboxButtons);
			}

			@Override
			public void newChatbox(ChatboxButton chatboxButton) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().addFirst(chatboxButton);
			}

			@Override
			public void deleteChatbox(ChatboxButton chatboxButton) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Add it in the chatbox
				chatboxContentArea.getChildren().remove(chatboxButton);
			}

			@Override
			public void onChatboxsUpdated(ArrayList<ChatboxButton> chatboxButtons) {
				// Add the initializeChatboxs
				VBox chatboxContentArea = sidebar.getChatboxsScrollContent();

				// Clear the current contentArea
				chatboxContentArea.getChildren().clear();
				
				// Add it in the chatbox
				chatboxContentArea.getChildren().addAll(chatboxButtons);
			}
		};

		// Create the chat controller with the listener
		chatController = new ChatController(chatListener);
	}
}
