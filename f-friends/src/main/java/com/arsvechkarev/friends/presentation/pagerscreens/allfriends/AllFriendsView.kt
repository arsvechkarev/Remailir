package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.presentation.BaseFriendsMvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AllFriendsView : BaseFriendsMvpView {
  
  fun showLoadingRemovingFromFriends(user: User)
  
  fun showSuccessRemovingFromFriends(user: User)
  
  fun showFailureRemovingFromFriends(user: User)
  
  fun showFriendAdded(user: User)
}