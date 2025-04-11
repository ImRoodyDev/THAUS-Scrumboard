package com.thaus.chatbox.interfaces;

import com.thaus.chatbox.types.ChatboxType;

public interface IChatboxFilterListener {
	void onFilterApplied(ChatboxType type);
	void onFilterRemoved(ChatboxType type);
	void onAllFiltersRemoved();
}
