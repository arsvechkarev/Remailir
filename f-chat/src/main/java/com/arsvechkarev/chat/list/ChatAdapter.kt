package com.arsvechkarev.chat.list

import com.arsvechkarev.chat.list.adapters.MessageOtherUserAdapterDelegate
import com.arsvechkarev.chat.list.adapters.MessageThisUserAdapterDelegate
import com.arsvechkarev.chat.list.adapters.TimeDividerAdapterDelegate
import com.arsvechkarev.core.model.messaging.ViewTypeConstants
import com.arsvechkarev.core.recycler.BaseListAdapter

class ChatAdapter : BaseListAdapter() {
  
  init {
    delegates.put(ViewTypeConstants.MESSAGE_THIS_USER, MessageThisUserAdapterDelegate())
    delegates.put(ViewTypeConstants.MESSAGE_OTHER_USER, MessageOtherUserAdapterDelegate())
    delegates.put(ViewTypeConstants.TIME_DIVIDER, TimeDividerAdapterDelegate())
  }
  
}