package firebase.messaging

import core.model.messaging.Message

interface MessageListener {
  
  fun receiveMessage(message: Message)
}