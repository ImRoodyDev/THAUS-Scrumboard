package com.thaus.chatbox.types;

public enum ScreenName {
	Home("Home"),
	Setting("Setting"),
	Login("Login"),
	Register("Register");


	private final String displayName;

	ScreenName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
