package com.arsvechkarev.chat.presentation

import com.arsvechkarev.chat.domain.ChatInteractor
import com.arsvechkarev.chat.domain.ChatState
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
    interactor.initialize(this, otherUserUsername)
    coroutine {
      when (type) {
        TYPE_REQUEST -> {
          viewState.showThisUserWaitingForChat()
          interactor.startRequest()
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
  
  fun allowBackPress(): Boolean {
    if (interactor.state == ChatState.CHATTING) {
      viewState.showExitRequestDialog()
      return false
    }
    return true
  }
  
  fun exit() {
    interactor.exitChatting()
    viewState.showExit()
  }
  
  override fun showOtherUserJoined() {
    viewState.showOtherUserJoined()
    interactor.startListeningForMessages()
  }
  
  override fun showOtherUserBecameInactive() {
  
  }
  
  override fun showOtherUserLeftChatting() {
    viewState.showOtherUserLeftChatting()
  }
  
  override fun showMessageSent(message: MessageThisUser) {
    viewState.showMessageSent(message)
  }
  
  override fun showMessageReceived(message: MessageOtherUser) {
    viewState.showMessageReceived(message)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    interactor.release()
  }
}