package com.arsvechkarev.chat.presentation

import com.arsvechkarev.chat.domain.ChatRepository
import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.messaging.DisplayableMessage
import com.arsvechkarev.core.model.messaging.Message
import com.arsvechkarev.core.model.messaging.MessageFactory
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatWaitingListener
import com.arsvechkarev.firebase.firestore.messaging.MessageListener

class ChatPresenter(
  private val thisUserUsername: String,
  private val messageFactory: MessageFactory,
  private val repository: ChatRepository,
  dispatchers: Dispatchers
) : BasePresenter<ChatView>(dispatchers) {
  
  private val messages = ArrayList<DisplayableMessage>()
  
  private val waitingListener = object : ChatWaitingListener {
    
    override fun onUserJoined() {
      viewState.showUserJoined()
    }
    
    override fun onUserCancelledRequest() {
      viewState.showUserCancelledJoiningRequest()
    }
  }
  
  private val messageListener = object : MessageListener {
    
    override fun receiveMessage(message: Message) {
      if (message.sender != thisUserUsername) {
        messages.add(DisplayableMessage(message.id, message.text))
        viewState.showMessageReceived(MessageOtherUser(message.id, message.text))
      }
    }
  }
  
  fun start(otherUserUsername: String) {
    coroutine {
      repository.setCurrentUserAsActive(otherUserUsername)
      repository.startWaitingForJoining(otherUserUsername, waitingListener)
    }
  }
  
  
  fun startListeningForMessages() {
    coroutine {
      repository.startListeningMessages(messageListener)
    }
  }
  
  fun sendMessage(message: String) {
    val msg = messageFactory.createMessage(message.trim(), thisUserUsername)
    coroutine {
      repository.sendMessage(msg)
      messages.add(DisplayableMessage(msg.id, msg.text))
      viewState.showMessageSent(MessageThisUser(msg.id, msg.text))
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    repository.releaseListeners()
  }
}