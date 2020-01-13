package com.arsvechkarev.messaging.list

import core.extensions.date
import core.extensions.time
import core.extensions.timeDividerFormat
import core.model.messaging.DialogMessage
import core.model.messaging.TimeDivider
import core.model.messaging.toOtherUser
import core.model.messaging.toThisUser
import core.recycler.DisplayableItem
import firebase.utils.randomUid
import log.log
import storage.AppUser

fun List<DialogMessage>.toDisplayableItems(): MutableList<DisplayableItem> {
  val displayMessages = ArrayList<DisplayableItem>()
  for (i in this.indices) {
    val message = this[i]
    log { "========================" }
    log { "${message.fromUserId} -> ${message.toUserId}: ${message.text}" }
    if (i == 0) {
      val timeDivider = TimeDivider(randomUid(), message.time.timeDividerFormat())
      displayMessages.add(timeDivider)
    } else {
      if (i != this.size - 1) {
        val nextMessage = this[i + 1]
        if (message.time.date != nextMessage.time.date) {
          val timeDivider = TimeDivider(randomUid(), message.time.timeDividerFormat())
          displayMessages.add(timeDivider)
        }
      }
    }
    when (AppUser.get().id) {
      message.fromUserId -> displayMessages.add(message.toThisUser())
      message.toUserId -> displayMessages.add(message.toOtherUser())
    }
  }
  return displayMessages
}