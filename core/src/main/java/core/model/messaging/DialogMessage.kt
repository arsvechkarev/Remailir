package core.model.messaging

data class DialogMessage(
  val fromUserId: String,
  val toUserId: String,
  val text: String,
  val timestamp: Long
) {
  constructor() : this("", "", "", 0)
}

fun DialogMessage.toChat(partialChat: PartialChat): Chat {
  return Chat(partialChat.otherUser, this)
}