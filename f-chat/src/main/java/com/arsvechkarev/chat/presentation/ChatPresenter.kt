package com.arsvechkarev.chat.presentation

import com.arsvechkarev.chat.domain.ChatRepository
import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.DisplayableMessage
import com.arsvechkarev.core.model.Message
import com.arsvechkarev.core.model.MessageOtherUser
import com.arsvechkarev.core.model.MessageThisUser
import com.arsvechkarev.firebase.firestore.chatting.MessageListener
import com.arsvechkarev.firebase.firestore.waiting.ChatWaitingListener
import com.google.firebase.Timestamp
import java.util.UUID

class ChatPresenter(
  private val repository: ChatRepository,
  private val thisUserUsername: String,
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
  
  
  fun start2() {
    coroutine {
      repository.startListeningMessages(messageListener)
    }
  }
  
  fun sendMessage(message: String) {
    coroutine {
      val msg = Message(UUID.randomUUID().toString(), message, thisUserUsername,
        Timestamp.now().toDate().time)
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