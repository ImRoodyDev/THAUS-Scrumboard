package com.thaus.chatbox.types;

import com.thaus.chatbox.interfaces.IMappedTypes;

public enum MemberType implements IMappedTypes {
	SCRUM_MASTER("Scrum Master"),
	PRODUCT_OWNER("Product Owner"),
	DEVELOPER("Developer"),
	MEMBER("Member");

	// Constructed enum nae
	private final String name;

	MemberType(String name) {
		this.name = name;
	}

	public static MemberType fromName(String name) {
		for (MemberType type : values()) {
			// Check if name is admin
			if (name.equals("admin")) {
				return MemberType.SCRUM_MASTER;
			}

			if (type.getName().equals(name)) {
				return type;
			}
		}

		return MemberType.MEMBER;
	}

	@Override
	public String getName() {
		return name;
	}
}
