package com.arsvechkarev.search.presentation

import com.arsvechkarev.search.domain.RequestResult
import core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchView : MvpView {
  
  fun showLoading()
  
  fun showUsersList(list: List<User>)
  
  fun showSearchResults(list: List<User>)
  
  fun showFailure(e: Throwable)
  
  fun showSendingRequest()
  
  fun showRequestSent()
  
  fun showSendingRequestFailure(result: RequestResult)
  
  fun showSendingRequestFailure(e: Throwable, user: User)
}