package com.arsvechkarev.chat.list

import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.recycler.DisplayableItem
import com.arsvechkarev.firebase.thisUserId
import java.util.UUID

fun List<DialogMessage>.toDisplayableItems(): MutableList<DisplayableItem> {
  val displayMessages = ArrayList<DisplayableItem>()
  this.forEach {
    when (thisUserId) {
      it.fromUserId -> displayMessages.add(
        MessageThisUser(
          UUID.randomUUID().toString(),
          it.text,
          it.timestamp
        )
      )
      it.toUserId -> displayMessages.add(
        MessageOtherUser(
          UUID.randomUUID().toString(),
          it.text,
          it.timestamp
        )
      )
    }
  }
  return displayMessages
}