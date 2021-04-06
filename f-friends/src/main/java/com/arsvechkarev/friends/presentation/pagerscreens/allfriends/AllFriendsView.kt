package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import com.arsvechkarev.friends.presentation.pagerscreens.BaseFriendsMvpView
import core.model.User
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AllFriendsView : BaseFriendsMvpView {
  
  fun showLoadingRemovingFromFriends(user: User)
  
  fun showSuccessRemovingFromFriends(user: User)
  
  fun showFailureRemovingFromFriends(user: User, error: Throwable)
  
  fun hideLayoutRemovingFromFriends()
  
  fun showNewFriendAdded(updatedFriends: List<User>)
}