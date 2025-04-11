package com.thaus.chatbox.interfaces;

import com.thaus.chatbox.types.ChatType;

public interface IChatFilterListener {
  void onFilterApplied(ChatType type);
  void onFilterRemoved(ChatType type);
  void onAllFiltersRemoved();
}
