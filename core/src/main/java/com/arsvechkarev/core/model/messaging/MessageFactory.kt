package com.arsvechkarev.core.model.messaging

import com.google.firebase.Timestamp
import java.util.UUID

interface MessageFactory {
  
  fun createMessage(text: String, sender: String): Message
}

object MessageFactoryImpl : MessageFactory {
  
  override fun createMessage(text: String, sender: String): Message {
    val id = UUID.randomUUID().toString()
    val timestamp = Timestamp.now().toDate().time
    return Message(id, text, sender, timestamp)
  }
}