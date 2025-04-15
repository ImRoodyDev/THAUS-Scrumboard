package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum ChatThreadType implements IMappedTypes {
	GENERAL("General"),
	FEATURES("Features"),
	SPRINTS("Sprints"),
	CUSTOM("Custom"),
	;

	// Name of the ChatThread type
	private final String name;

	// Constructor
	ChatThreadType(String name) {
		this.name = name;
	}

	public static ChatThreadType fromName(String name) {
		for (ChatThreadType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		return CUSTOM;
	}

	@Override
	public String getName() {
		return name;
	}

}
