package com.thaus.chatbox.types;

public enum ScreenName {
	Home("Home"),
	Authentication("Authentication");
	
	private final String displayName;

	ScreenName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
