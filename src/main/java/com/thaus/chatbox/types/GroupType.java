package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum GroupType implements IMappedTypes {
	TEAM("Team"),
	USER("Organisation"),
	UNKOWN("Unknown");

	private final String name;

	GroupType(String name) {
		this.name = name;
	}

	public static GroupType fromName(String name) {
		for (GroupType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}

		return UNKOWN;
	}

	@Override
	public String getName() {
		return name;
	}
}
