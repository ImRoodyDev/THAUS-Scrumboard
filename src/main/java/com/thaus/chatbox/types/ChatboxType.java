package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum ChatboxType implements IMappedTypes {
	TEAM("Teams"),
 	USER("Users"),
	FRIEND("Friends"),;

	private final String name;

	ChatboxType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
