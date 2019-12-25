package core.model.messaging

import core.recycler.DisplayableItem

data class MessageThisUser(
  override val id: String,
  val text: String,
  val timestamp: Long
) : DisplayableItem {
  override val type = TYPE_MESSAGE_THIS_USER
}