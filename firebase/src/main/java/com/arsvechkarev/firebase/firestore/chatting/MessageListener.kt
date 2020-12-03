package com.arsvechkarev.firebase.firestore.chatting

import com.arsvechkarev.core.model.Message

interface MessageListener {
  
  fun receiveMessage(message: Message)
}