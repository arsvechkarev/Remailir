package core.model.messaging

import core.model.users.User
import core.recycler.DisplayableItem
import log.log

data class PartialChat(
  val memberIds: List<String>,
  val userOne: User,
  val userTwo: User
) {
  constructor() : this(listOf(), User(), User())
}

data class Chat(
  val otherUser: User,
  val lastMessage: DialogMessage?
) : DisplayableItem {
  
  override val id = otherUser.id
  
  class ChatCallback : DisplayableItem.DiffCallBack<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
      val same = super.areItemsTheSame(oldItem, newItem)
      log { "chats ids are same = $same" }
      return same
    }
    
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
      val same = super.areContentsTheSame(oldItem, newItem)
      log { "chats contents are same = $same" }
      return same
    }
  }
}
