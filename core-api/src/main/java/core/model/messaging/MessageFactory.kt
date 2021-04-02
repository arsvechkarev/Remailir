package core.model.messaging

interface MessageFactory {
  
  /**
   * Produces messages for text and sender
   *
   * @param text Message text
   * @param sender Sender's username
   */
  fun createMessage(text: String, sender: String): Message
}