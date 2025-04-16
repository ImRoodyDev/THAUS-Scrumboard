package com.thaus.chatbox.classes;

import java.util.Date;

public class Member {
	private String username;
	private Date joinedAt;
	private String role;

	public Member(String username, Date joinedAt, String role) {
		this.username = username;
		this.joinedAt = joinedAt;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public Date getJoinedAt() {
		return joinedAt;
	}

	public String getRole() {
		return role;
	}
}
