package com.thaus.chatbox.classes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Epic {
	private String id;
	private String name;

	// Observable properties
	private ObservableList<Story> stories = javafx.collections.FXCollections.observableArrayList();
	private IntegerProperty storyCount = new SimpleIntegerProperty(0);

	public Epic(String id, String name) {
		this.id = id;
		this.name = name;

		// Bind story count to the size of the stories list
		storyCount.bind(Bindings.size(stories));
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public IntegerProperty getStoryCount() {
		return storyCount;
	}

	public ObservableList<Story> getStories() {
		return stories;
	}

	public void addStory(Story story) {
		stories.add(story);
	}

	public void removeStory(Story story) {
		stories.remove(story);
	}

	public void addStories(ArrayList<Story> stories) {
		this.stories.addAll(stories);
	}
}
