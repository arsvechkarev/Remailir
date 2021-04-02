package firebase.messaging

import core.model.messaging.Message

interface MessagingDataSource {
  
  suspend fun sendMessage(message: Message)
  
  fun listenForMessages(listener: MessageListener)
  
  fun releaseListener()
}