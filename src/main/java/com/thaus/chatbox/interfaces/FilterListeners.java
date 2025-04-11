package com.thaus.chatbox.interfaces;

import com.thaus.chatbox.types.ChatType;

public interface FilterListeners {
  void onFilterApplied(ChatType type);
  void onFilterRemoved(ChatType type);
  void onAllFiltersRemoved();
}
