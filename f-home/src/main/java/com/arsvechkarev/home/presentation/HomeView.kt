package com.arsvechkarev.home.presentation

import com.arsvechkarev.core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeView : MvpView {
  
  fun showLoadingUsersThatWaitingForChat()
  
  fun showWaitingToChatList(list: List<User>)
  
  fun showNobodyWaitingForChat()
  
  fun showErrorLoadingWaitingToChat(e: Throwable)
  
  fun showGoToChat(user: User)
  
  fun showFailedToRespondToChat(user: User)
}