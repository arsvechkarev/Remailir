package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FriendsView : MvpView {
  
  fun showLoading()
  
  fun showNoFriends()
  
  fun showFriendsList(list: List<User>)
  
  fun showFailure(e: Throwable)
}