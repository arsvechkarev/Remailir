package com.arsvechkarev.home.presentation

import core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeView : MvpView {
  
  fun showLoadingUsersWaitingToChat()
  
  fun showSuccessUsersWaitingToChat(list: List<User>)
  
  fun showEmptyUsersWaitingToChat()
  
  fun showErrorUsersWaitingToChat(e: Throwable)
  
  fun showFailedToRespondToChat(user: User)
  
  fun showOpenMenu()
  
  fun showCloseMenu()
}