package com.thaus.chatbox.controllers;

import com.thaus.chatbox.components.interactive.ChatboxFilterButton;
import com.thaus.chatbox.interfaces.IChatboxFilterListener;
import com.thaus.chatbox.types.ChatboxType;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatboxFilterController {
	// Array of Filter button
	private final ArrayList<ChatboxFilterButton> filterButtons = new ArrayList<>();
	// Filter Listener
	private final IChatboxFilterListener filterListeners;
	// Filter flow pane
	private final FlowPane filtersFlowPane;

	// Constructor
	public ChatboxFilterController(FlowPane filtersFlowPane, IChatboxFilterListener filterListeners) {
		this.filterListeners = filterListeners;
		this.filtersFlowPane = filtersFlowPane;
	}

	public ChatboxFilterController(FlowPane filtersFlowPane, IChatboxFilterListener filterListeners, boolean forceClean) {
		this.filterListeners = filterListeners;
		this.filtersFlowPane = filtersFlowPane;
		filtersFlowPane.getChildren().clear();
	}

	// Get the list of currently applied filter types
	public List<ChatboxType> appliedFilters() {
		return filterButtons.stream()
				.map(ChatboxFilterButton::getType)
				.collect(Collectors.toList());
	}

	// Remove all filters
	public void clearAllFilters() {
		// Iterate through a copy to avoid ConcurrentModificationException
		new ArrayList<>(filterButtons).forEach(button -> filtersFlowPane.getChildren().remove(button.onDestroy()));
		filterButtons.clear();
		filterListeners.onAllFiltersRemoved();
	}

	// Add filter
	public void applyFilter(ChatboxType filter) {
		// Check if the filter is already applied
		if (filterButtons.stream().anyMatch(button -> button.getType() == filter)) {
			System.out.println("Filter already applied!");
			return; // Don't add duplicate filters
		}

		// Create a Filter button
		ChatboxFilterButton filterButton = new ChatboxFilterButton(filter);

		// Add filter buttons in array
		filterButtons.add(filterButton);

		// Add the filters in the Flow Pane
		filtersFlowPane.getChildren().addFirst(filterButton.getNode());

		// Call on filter applied event
		filterListeners.onFilterApplied(filter);

		// Apply event on button clicked to remove filter
		filterButton.onClick(() -> {
			filterListeners.onFilterRemoved(filter); // Notify listener of removal
			removeFilterButton(filterButton);
		});
	}

	// Open filter Context menu
	public void enableContextMenu(boolean enable) {

	}

	// Remove a specific filter button
	private void removeFilterButton(ChatboxFilterButton filterButtonToRemove) {
		if (filterButtonToRemove != null) {
			filtersFlowPane.getChildren().remove(filterButtonToRemove.onDestroy());
			filterButtons.remove(filterButtonToRemove);
		}
	}
}
