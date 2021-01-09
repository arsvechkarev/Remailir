package com.arsvechkarev.firebase.firestore.messaging

import com.arsvechkarev.core.model.messaging.Message

interface MessagingDataSource {
  
  suspend fun sendMessage(message: Message)
  
  fun listenForMessages(listener: MessageListener)
  
  fun releaseListener()
}