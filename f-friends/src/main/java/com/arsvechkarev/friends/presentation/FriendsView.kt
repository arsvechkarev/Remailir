package com.arsvechkarev.friends.presentation

import core.model.FriendsType
import core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FriendsView : MvpView {
  
  fun showActionDialog(friendsType: FriendsType, user: User)
  
  fun hideActionDialog()
  
  fun showRemovingFriendConfirmationDialog(user: User)
  
  fun hideRemovingFriendConfirmationDialog()
  
  fun onRemovingFromScreen()
}