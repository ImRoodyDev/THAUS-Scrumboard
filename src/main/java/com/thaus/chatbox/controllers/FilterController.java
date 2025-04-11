package com.thaus.chatbox.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.thaus.chatbox.components.FilterButton;
import com.thaus.chatbox.interfaces.FilterListeners;
import com.thaus.chatbox.types.ChatType;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class FilterController {
	// Array of Filter button
	private final ArrayList<FilterButton> filterButtons = new ArrayList<>();
	// Filter Listener
	private final FilterListeners filterListeners;

	// Filter Components
	@FXML
	private FlowPane filtersFlowPane;

	// Constructor
	public FilterController(FilterListeners filterListeners) {
		this.filterListeners = filterListeners;
	}

	// Initialize component
	public void initialize() {}
	// Get the list of currently applied filter types
	public List<ChatType> appliedFilters() {
		return filterButtons.stream()
				.map(FilterButton::getType)
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
	public void applyFilter(ChatType filter) {
		// Check if the filter is already applied
		if (filterButtons.stream().anyMatch(button -> button.getType() == filter)) {
			System.out.println("Filter already applied!");
			return; // Don't add duplicate filters
		}

		// Create a Filter button
		FilterButton filterButton = new FilterButton(filter);

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
	private void removeFilterButton(FilterButton filterButtonToRemove) {
		if (filterButtonToRemove != null) {
			filtersFlowPane.getChildren().remove(filterButtonToRemove.onDestroy());
			filterButtons.remove(filterButtonToRemove);
		}
	}
}
