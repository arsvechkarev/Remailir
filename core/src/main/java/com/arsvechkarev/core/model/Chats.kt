package com.arsvechkarev.core.model

import androidx.recyclerview.widget.DiffUtil
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.users.OtherUser

data class PartialChatOld(
  val memberIds: List<String>,
  val otherUserId: String
) {
  constructor() : this(listOf(), "")
}

data class PartialChat(
  val memberIds: List<String>,
  val otherUser: OtherUser
) {
  constructor() : this(listOf(), OtherUser())
}

data class Chat(
  val otherUser: OtherUser,
  val lastMessage: DialogMessage?
) {
  
  class DiffCallBack : DiffUtil.ItemCallback<Chat>() {
    
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
      return oldItem.otherUser.id == newItem.otherUser.id
    }
    
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
      return oldItem == newItem
    }
    
  }
}
