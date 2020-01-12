package core.model.messaging

import core.model.users.User
import core.recycler.DisplayableItem

data class PartialChat(
  val memberIds: List<String>,
  val otherUser: User
) {
  constructor() : this(listOf(), User())
}

data class Chat(
  val otherUser: User,
  val lastMessage: DialogMessage?
) : DisplayableItem {
  
  override val id = otherUser.id
}
