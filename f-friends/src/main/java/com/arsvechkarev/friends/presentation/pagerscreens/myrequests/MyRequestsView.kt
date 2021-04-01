package com.arsvechkarev.friends.presentation.pagerscreens.myrequests

import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.presentation.BaseFriendsMvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyRequestsView : BaseFriendsMvpView {
  
  fun showLoadingCancelMyRequest(user: User)
  
  fun showSuccessCancelMyRequest(user: User)
  
  fun showFailureCancelMyRequest(user: User)
}