package com.arsvechkarev.chat.domain

import com.arsvechkarev.core.model.Message
import com.arsvechkarev.firebase.firestore.chatting.ChattingDataSource
import com.arsvechkarev.firebase.firestore.chatting.MessageListener
import com.arsvechkarev.firebase.firestore.waiting.ChatWaitingDataSource
import com.arsvechkarev.firebase.firestore.waiting.ChatWaitingListener

class ChatRepository(
  private val chatWaitingDataSource: ChatWaitingDataSource,
  private val chattingDataSource: ChattingDataSource
) {
  
  suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    chatWaitingDataSource.setCurrentUserAsActive(otherUserUsername)
  }
  
  suspend fun startWaitingForJoining(otherUserUsername: String, listener: ChatWaitingListener) {
    chatWaitingDataSource.waitForJoining(otherUserUsername, listener)
  }
  
  suspend fun startListeningMessages(listener: MessageListener) {
    chattingDataSource.listenForMessages(listener)
  }
  
  suspend fun sendMessage(message: Message) {
    chattingDataSource.sendMessage(message)
  }
  
  fun releaseListeners() {
    chatWaitingDataSource.releaseJoiningListener()
    chattingDataSource.releaseListener()
  }
}