package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FriendsView : MvpView {
  
  fun showLoading(type: FriendsType)
  
  fun showNoData(type: FriendsType)
  
  fun showList(type: FriendsType, list: List<User>)
  
  fun showSwitchedToList(type: FriendsType, list: List<User>)
  
  fun showSwitchedToEmptyView(type: FriendsType)
  
  fun showFailure(e: Throwable)
}