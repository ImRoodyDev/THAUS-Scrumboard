package com.thaus.chatbox.classes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Feature {
	private String id;
	private String name;

	// Observable properties
	private IntegerProperty epicCount = new SimpleIntegerProperty(0);
	private IntegerProperty storyCount = new SimpleIntegerProperty(0);
	private ObservableList<Epic> epics = javafx.collections.FXCollections.observableArrayList();

	public Feature(String id, String name) {
		this.id = id;
		this.name = name;

		// Bind epic count to the size of the epics list
		epicCount.bind(Bindings.size(epics));
		epics.addListener((ListChangeListener<Epic>) change -> updateStoryCount());
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public IntegerProperty getEpicCount() {
		return epicCount;
	}

	public IntegerProperty getStoryCount() {
		return storyCount;
	}

	public ObservableList<Epic> getEpics() {
		return epics;
	}


	public void addEpic(Epic epic) {
		epics.add(epic);
	}

	public void addEpics(ArrayList<Epic> epics) {
		this.epics.addAll(epics);
	}

	public void removeEpic(Epic epic) {
		epics.remove(epic);
	}

	private void updateStoryCount() {
		int totalStories = epics.stream().mapToInt(epic -> epic.getStoryCount().get()).sum();
		storyCount.set(totalStories);
	}
}
