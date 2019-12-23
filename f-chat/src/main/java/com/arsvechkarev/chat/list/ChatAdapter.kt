package com.arsvechkarev.chat.list

import com.arsvechkarev.chat.list.adapters.MessageOtherUserAdapterDelegate
import com.arsvechkarev.chat.list.adapters.MessageThisUserAdapterDelegate
import com.arsvechkarev.chat.list.adapters.TimeDividerAdapterDelegate
import core.model.messaging.ViewTypeConstants
import core.recycler.BaseAdapter

class ChatAdapter : BaseAdapter() {
  
  init {
    delegates.put(ViewTypeConstants.MESSAGE_THIS_USER, MessageThisUserAdapterDelegate())
    delegates.put(ViewTypeConstants.MESSAGE_OTHER_USER, MessageOtherUserAdapterDelegate())
    delegates.put(ViewTypeConstants.TIME_DIVIDER, TimeDividerAdapterDelegate())
  }
  
}