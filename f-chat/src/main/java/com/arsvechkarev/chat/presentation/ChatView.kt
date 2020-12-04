package com.arsvechkarev.chat.presentation

import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChatView : MvpView {
  
  fun showUserJoined()
  
  fun showMessageReceived(messageOtherUser: MessageOtherUser)
  
  fun showMessageSent(messageThisUser: MessageThisUser)
  
  fun showUserCancelledJoiningRequest()
}