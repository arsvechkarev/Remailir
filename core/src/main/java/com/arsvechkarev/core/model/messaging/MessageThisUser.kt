package com.arsvechkarev.core.model.messaging

import com.arsvechkarev.core.recycler.DisplayableItem

data class MessageThisUser(
  override val id: String,
  val text: String,
  val timestamp: Long
) : DisplayableItem {
  override val type = ViewTypeConstants.MESSAGE_THIS_USER
}