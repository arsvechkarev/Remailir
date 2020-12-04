package com.arsvechkarev.chat.domain

import com.arsvechkarev.core.model.messaging.Message
import com.arsvechkarev.firebase.firestore.messaging.MessagingDataSource
import com.arsvechkarev.firebase.firestore.messaging.MessageListener
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingDataSource
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingListener

class ChatRepository(
  private val chatWaitingDataSource: ChatWaitingDataSource,
  private val messagingDataSource: MessagingDataSource
) {
  
  suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    chatWaitingDataSource.setCurrentUserAsActive(otherUserUsername)
  }
  
  suspend fun startWaitingForJoining(otherUserUsername: String, listener: ChatWaitingListener) {
    chatWaitingDataSource.waitForJoining(otherUserUsername, listener)
  }
  
  suspend fun startListeningMessages(listener: MessageListener) {
    messagingDataSource.listenForMessages(listener)
  }
  
  suspend fun sendMessage(message: Message) {
    messagingDataSource.sendMessage(message)
  }
  
  fun releaseListeners() {
    chatWaitingDataSource.releaseJoiningListener()
    messagingDataSource.releaseListener()
  }
}