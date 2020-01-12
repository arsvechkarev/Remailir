package com.arsvechkarev.chat.list

import com.arsvechkarev.chat.list.delegates.MessageOtherUserAdapterDelegate
import com.arsvechkarev.chat.list.delegates.MessageThisUserAdapterDelegate
import com.arsvechkarev.chat.list.delegates.TimeDividerAdapterDelegate
import core.model.messaging.TYPE_MESSAGE_OTHER_USER
import core.model.messaging.TYPE_MESSAGE_THIS_USER
import core.model.messaging.TYPE_TIME_DIVIDER
import core.recycler.BaseAdapter
import core.recycler.DisplayableItem

class MessagingAdapter : BaseAdapter<DisplayableItem>() {
  
  init {
    delegates.put(TYPE_MESSAGE_THIS_USER, MessageThisUserAdapterDelegate())
    delegates.put(TYPE_MESSAGE_OTHER_USER, MessageOtherUserAdapterDelegate())
    delegates.put(TYPE_TIME_DIVIDER, TimeDividerAdapterDelegate())
  }
  
}