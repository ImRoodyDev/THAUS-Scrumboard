package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum ChatType implements IMappedTypes {
	GENERAL("General"),
	EPICS("Epics"),
	USER_STORY("User story"),
	SPRINTS("Sprints"),
	;
	// Constructed enum nae
	private final String name;

	ChatType(String name) {
		this.name = name;
	}

	public static ChatType fromName(String name) {
		for (ChatType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		throw new Error("Invalid ChatType: " + name);
	}

	@Override
	public String getName() {
		return name;
	}
}
