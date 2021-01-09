package com.arsvechkarev.chat.presentation

import com.arsvechkarev.chat.domain.ChatInteractor
import com.arsvechkarev.chat.domain.ChattingObserver
import com.arsvechkarev.chat.domain.TYPE_JOINED
import com.arsvechkarev.chat.domain.TYPE_REQUEST
import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser

class ChatPresenter(
  private val interactor: ChatInteractor,
  dispatchers: Dispatchers
) : BasePresenter<ChatView>(dispatchers), ChattingObserver {
  
  fun initialize(type: String, otherUserUsername: String) {
    interactor.initialize(this)
      coroutine {
      when (type) {
        TYPE_REQUEST -> {
          viewState.showThisUserWaitingForChat()
          interactor.startRequest(otherUserUsername)
        }
        TYPE_JOINED -> {
          viewState.showThisUserJoined()
          interactor.startListeningForMessages()
        }
        else -> throw IllegalStateException()
      }
    }
  }
  
  fun sendMessage(message: String) {
    coroutine {
      interactor.sendMessage(message)
    }
  }
  
  override fun showOtherUserJoined() {
    viewState.showOtherUserJoined()
    interactor.startListeningForMessages()
  }
  
  override fun showOtherUserCancelledRequest() {
  
  }
  
  override fun showMessageSent(message: MessageThisUser) {
    viewState.showMessageSent(message)
  }
  
  override fun showMessageReceived(message: MessageOtherUser) {
    viewState.showMessageReceived(message)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    interactor.releaseListeners()
  }
}