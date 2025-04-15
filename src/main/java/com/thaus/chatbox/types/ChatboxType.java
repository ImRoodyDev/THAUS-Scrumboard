package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum ChatboxType implements IMappedTypes {
	TEAM("Team"),
	USER("Organisation");

	private final String name;

	ChatboxType(String name) {
		this.name = name;
	}

	public static ChatboxType fromName(String name) {
		for (ChatboxType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No enum found with name: " + name);
	}

	@Override
	public String getName() {
		return name;
	}
}
