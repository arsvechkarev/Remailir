package com.arsvechkarev.core.model.messaging

import com.arsvechkarev.core.recycler.DisplayableItem
import com.arsvechkarev.core.recycler.ViewTypeConstants


data class MessageOtherUser(
  override val id: String,
  val text: String
) : DisplayableItem {
  override val type = ViewTypeConstants.MESSAGE_OTHER_USER
}