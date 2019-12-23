package core.model.messaging

data class OneToOneChat(
  val userOneId: String,
  val userTwoId: String,
  val createdAt: Long
)