package com.arsvechkarev.core.model.messaging

import com.google.firebase.Timestamp
import java.util.UUID

interface MessageFactory {
  
  /**
   * Produces messages for text and sender
   *
   * @param text Message text
   * @param sender Sender's username
   */
  fun createMessage(text: String, sender: String): Message
}

object DefaultMessageFactory : MessageFactory {
  
  override fun createMessage(text: String, sender: String): Message {
    val id = UUID.randomUUID().toString()
    val timestamp = Timestamp.now().toDate().time
    return Message(id, text, sender, timestamp)
  }
}