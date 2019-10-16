package com.arsvechkarev.chat.list

import com.arsvechkarev.chat.list.adapters.MessageThisUserAdapterDelegate
import com.arsvechkarev.chat.list.adapters.MessageOtherUserAdapterDelegate
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.recycler.BaseAdapter
import com.arsvechkarev.core.recycler.ViewTypeConstants
import com.arsvechkarev.firebase.thisUserId
import java.lang.IllegalStateException
import java.util.UUID

class ChatAdapter : BaseAdapter() {
  
  init {
    delegates.put(ViewTypeConstants.MESSAGE_THIS_USER, MessageThisUserAdapterDelegate())
    delegates.put(ViewTypeConstants.MESSAGE_OTHER_USER, MessageOtherUserAdapterDelegate())
  }
  
}