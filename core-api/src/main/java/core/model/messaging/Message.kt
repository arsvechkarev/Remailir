package core.model.messaging

/**
 * A message from backend
 *
 * @param id Unique message id
 * @param text Message text
 * @param sender Sender's username
 * @param timestamp Message timestamp in milliseconds
 */
data class Message(
  val id: String,
  val text: String,
  val sender: String,
  val timestamp: Long
)