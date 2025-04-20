package com.thaus.chatbox.classes;

import com.thaus.chatbox.types.MemberType;
import com.thaus.chatbox.utils.DateUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Member {
	private String memberId;
	private Date joinedAt;

	// Observable properties
	private StringProperty username = new javafx.beans.property.SimpleStringProperty();
	private ObjectProperty<MemberType> role = new SimpleObjectProperty<>();


	public Member(String memberId, String username, Date joinedAt, String role) {
		this.memberId = memberId;
		this.joinedAt = joinedAt;

		this.role.set(MemberType.fromName(role));
		this.username.set(username);
	}

	public String getId() {
		return memberId;
	}

	public StringProperty getUsername() {
		return username;
	}

	public String getJoinedAt() {
		return DateUtils.formatDate(joinedAt);
	}

	public ObjectProperty<MemberType> getRole() {
		return role;
	}
}
