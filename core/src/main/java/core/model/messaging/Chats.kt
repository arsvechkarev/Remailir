package core.model.messaging

import core.model.users.OtherUser
import core.recycler.DisplayableItem

data class PartialChat(
  val memberIds: List<String>,
  val otherUser: OtherUser
) {
  constructor() : this(listOf(), OtherUser())
}

data class Chat(
  val otherUser: OtherUser,
  val lastMessage: DialogMessage?
) : DisplayableItem {
  
  override val id = otherUser.id
}
