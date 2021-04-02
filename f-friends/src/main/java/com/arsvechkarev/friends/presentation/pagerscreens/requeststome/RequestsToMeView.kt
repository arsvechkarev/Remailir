package com.arsvechkarev.friends.presentation.pagerscreens.requeststome

import com.arsvechkarev.friends.presentation.BaseFriendsMvpView
import core.model.User
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RequestsToMeView : BaseFriendsMvpView {
  
  fun showLoadingAcceptingRequest(user: User)
  
  fun showSuccessAcceptingRequest(user: User)
  
  fun showFailureAcceptingRequest(user: User)
  
  
  fun showLoadingDismissingRequest(user: User)
  
  fun showSuccessDismissingRequest(user: User)
  
  fun showFailureDismissingRequest(user: User)
}