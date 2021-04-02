package firebase.impl

import com.google.firebase.Timestamp
import core.model.messaging.Message
import core.model.messaging.MessageFactory
import java.util.UUID

object DefaultMessageFactory : MessageFactory {
  
  override fun createMessage(text: String, sender: String): Message {
    val id = UUID.randomUUID().toString()
    val timestamp = Timestamp.now().toDate().time
    return Message(id, text, sender, timestamp)
  }
}