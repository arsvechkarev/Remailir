package com.arsvechkarev.chat.domain

import com.arsvechkarev.chat.domain.ChatState.CHATTING
import com.arsvechkarev.chat.domain.ChatState.NONE
import com.arsvechkarev.chat.domain.ChatState.OTHER_USER_LEFT
import com.arsvechkarev.chat.domain.ChatState.WAITING_FOR_USER
import com.arsvechkarev.core.model.messaging.DisplayableMessage
import com.arsvechkarev.core.model.messaging.Message
import com.arsvechkarev.core.model.messaging.MessageFactory
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatMetaInfoDataSource
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingListener
import com.arsvechkarev.firebase.firestore.messaging.MessageListener
import com.arsvechkarev.firebase.firestore.messaging.MessagingDataSource

class ChatInteractor(
  private val thisUserUsername: String,
  private val messageFactory: MessageFactory,
  private val chatMetaInfoDataSource: ChatMetaInfoDataSource,
  private val messagingDataSource: MessagingDataSource,
) {
  
  var state = NONE
    private set
  
  private val messages = ArrayList<DisplayableMessage>()
  private var observer: ChattingObserver? = null
  private var otherUserUsername: String? = null
  
  private val waitingListener = object : ChatWaitingListener {
    
    override fun onUserBecameActive() {
      if (state == NONE || state == WAITING_FOR_USER) {
        state = CHATTING
        observer?.showOtherUserJoined()
      }
    }
    
    override fun onUserBecameInactive() {
      when (state) {
        WAITING_FOR_USER -> {
          observer?.showOtherUserBecameInactive()
          state = NONE
        }
        CHATTING -> {
          observer?.showOtherUserLeftChatting()
        }
        OTHER_USER_LEFT -> {
    
        }
      }
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
  
  fun initialize(observer: ChattingObserver, otherUserUsername: String) {
    this.observer = observer
    this.otherUserUsername = otherUserUsername
  }
  
  fun startListeningForMessages() {
    state = CHATTING
    messagingDataSource.listenForMessages(messageListener)
  }
  
  suspend fun sendMessage(message: String) {
    val msg = messageFactory.createMessage(message.trim(), thisUserUsername)
    messagingDataSource.sendMessage(msg)
    observer?.showMessageSent(MessageThisUser(msg.id, msg.text))
  }
  
  suspend fun startRequest() {
    state = WAITING_FOR_USER
    setCurrentUserAsActive(otherUserUsername!!)
    startWaitingForJoining(otherUserUsername!!, waitingListener)
  }
  
  fun exitChatting() {
    chatMetaInfoDataSource.setCurrentUserAsInactive(otherUserUsername!!)
    release()
  }
  
  fun release() {
    state = NONE
    chatMetaInfoDataSource.releaseJoiningListener()
    messagingDataSource.releaseListener()
    observer = null
  }
  
  private suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    chatMetaInfoDataSource.setCurrentUserAsActive(otherUserUsername)
  }
  
  private suspend fun startWaitingForJoining(otherUserUsername: String, listener: ChatWaitingListener) {
    chatMetaInfoDataSource.waitForJoining(otherUserUsername, listener)
  }
}