package core.model.messaging

import core.extensions.randomUid

data class DialogMessage(
  val fromUserId: String,
  val toUserId: String,
  val text: String,
  val timestamp: Long
) {
  constructor() : this("", "", "", 0)
}

fun DialogMessage.toThisUser(): MessageThisUser {
  return MessageThisUser(randomUid(), text, timestamp)
}

fun DialogMessage.toOtherUser(): MessageOtherUser {
  return MessageOtherUser(randomUid(), text, timestamp)
}