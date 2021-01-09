package com.arsvechkarev.chat.domain

import com.arsvechkarev.core.model.messaging.DisplayableMessage
import com.arsvechkarev.core.model.messaging.Message
import com.arsvechkarev.core.model.messaging.MessageFactory
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingDataSource
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingListener
import com.arsvechkarev.firebase.firestore.messaging.MessageListener
import com.arsvechkarev.firebase.firestore.messaging.MessagingDataSource

class ChatInteractor(
  private val thisUserUsername: String,
  private val messageFactory: MessageFactory,
  private val chatWaitingDataSource: ChatWaitingDataSource,
  private val messagingDataSource: MessagingDataSource,
) {
  
  @Volatile
  private var isChatting = false
  private val messages = ArrayList<DisplayableMessage>()
  private var observer: ChattingObserver? = null
  
  private val waitingListener = object : ChatWaitingListener {
    
    override fun onUserJoined() {
      observer?.showOtherUserJoined()
    }
    
    override fun onUserCancelledRequest() {
      observer?.showOtherUserCancelledRequest()
    }
  }
  
  private val messageListener = object : MessageListener {
    
    override fun receiveMessage(message: Message) {
      if (message.sender != thisUserUsername) {
        messages.add(DisplayableMessage(message.id, message.text))
        observer?.showMessageReceived(MessageOtherUser(message.id, message.text))
      }
    }
  }
  
  fun initialize(observer: ChattingObserver) {
    this.observer = observer
  }
  
  fun startListeningForMessages() {
    messagingDataSource.listenForMessages(messageListener)
  }
  
  suspend fun sendMessage(message: String) {
    val msg = messageFactory.createMessage(message.trim(), thisUserUsername)
    messagingDataSource.sendMessage(msg)
    observer?.showMessageSent(MessageThisUser(msg.id, msg.text))
  }
  
  suspend fun startRequest(otherUserUsername: String) {
    setCurrentUserAsActive(otherUserUsername)
    startWaitingForJoining(otherUserUsername, waitingListener)
  }
  
  fun releaseListeners() {
    chatWaitingDataSource.releaseJoiningListener()
    messagingDataSource.releaseListener()
    observer = null
  }
  
  private suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    chatWaitingDataSource.setCurrentUserAsActive(otherUserUsername)
  }
  
  private suspend fun startWaitingForJoining(otherUserUsername: String, listener: ChatWaitingListener) {
    chatWaitingDataSource.waitForJoining(otherUserUsername, listener)
  }
}