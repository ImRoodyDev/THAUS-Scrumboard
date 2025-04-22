package com.thaus.chatbox.classes;

import com.thaus.chatbox.utils.DateUtils;

import java.util.Date;

public class Message {
	private String username;
	private String content;
	private String timestamp;
	private boolean isMine;
	private Date date;

	public Message(String username, String content, Date timestamp, boolean you) {
		this.username = username;
		this.content = content;
		this.date = timestamp;
		this.timestamp = DateUtils.formatDate(timestamp);
		this.isMine = you;
	}

	public Message(String content) {
		this.content = content;
		this.timestamp = DateUtils.formatDate(new Date());
		isMine = true;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getSenderName() {
		return username;
	}

	public boolean isMine() {
		return isMine;
	}
}
