package com.arsvechkarev.firebase.firestore.chatting

import com.arsvechkarev.core.model.Message

interface ChattingDataSource {
  
  suspend fun sendMessage(message: Message)
  
  suspend fun listenForMessages(listener: MessageListener)
  
  fun releaseListener()
}