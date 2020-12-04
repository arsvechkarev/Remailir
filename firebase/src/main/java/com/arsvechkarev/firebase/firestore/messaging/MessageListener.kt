package com.arsvechkarev.firebase.firestore.messaging

import com.arsvechkarev.core.model.messaging.Message

interface MessageListener {
  
  fun receiveMessage(message: Message)
}