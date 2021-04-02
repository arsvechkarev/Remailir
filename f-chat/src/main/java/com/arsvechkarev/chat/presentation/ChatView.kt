package com.arsvechkarev.chat.presentation

import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatView : MvpView {
  
  fun showThisUserWaitingForChat()
  
  fun showThisUserJoined()
  
  fun showOtherUserJoined()
  
  fun showMessageReceived(messageOtherUser: MessageOtherUser)
  
  fun showMessageSent(messageThisUser: MessageThisUser)
  
  fun showOtherUserCancelledRequest()
  
  fun showOtherUserLeftChatting()
  
  fun showExitRequestDialog()
  
  fun showExit()
}