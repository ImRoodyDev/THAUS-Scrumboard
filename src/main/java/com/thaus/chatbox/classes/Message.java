package com.thaus.chatbox.classes;

public class Message {
	private String senderId;
	private String senderName;
	private String content;
	private String timestamp;
	private boolean isMine;

	public Message(String sender, String senderId, String content, String timestamp) {
		this.senderId = senderId;
		this.senderName = sender;
		this.content = content;
		this.timestamp = timestamp;
	}

	public Message(String content) {
		this.content = content;
		this.timestamp = String.valueOf(System.currentTimeMillis());
		isMine = true;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getContent() {
		return content;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getSenderName() {
		return senderName;
	}

	public boolean isMine() {
		return isMine;
	}

}
