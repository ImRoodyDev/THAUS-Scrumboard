package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.MappedTypes;

public enum ChatType implements MappedTypes {
	EPICS("Epics"),
	USER_STORY("User story"),
	SPRINTS("Sprints"),
	GENERAL("General");

	private final String name;

	ChatType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
