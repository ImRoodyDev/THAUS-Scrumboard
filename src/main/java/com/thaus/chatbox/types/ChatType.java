package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum ChatType implements IMappedTypes {
	EPICS("Epics"),
	USER_STORY("User story"),
	SPRINTS("Sprints"),
	GENERAL("General");

	// Constructed enum nae
	private final String name;

	ChatType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
