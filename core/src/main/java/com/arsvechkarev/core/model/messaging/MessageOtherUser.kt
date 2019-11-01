package com.arsvechkarev.core.model.messaging

import com.arsvechkarev.core.recycler.DisplayableItem


data class MessageOtherUser(
  override val id: String,
  val text: String,
  val timestamp: Long
) : DisplayableItem {
  override val type = ViewTypeConstants.MESSAGE_OTHER_USER
}