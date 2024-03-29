package com.arsvechkarev.friends.presentation.pagerscreens

import core.model.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Represents base view for friends screens
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseFriendsMvpView : MvpView {
  
  fun showLoadingList()
  
  fun showListIsEmpty()
  
  fun showLoadedList(data: List<User>)
  
  fun showListChanged(newData: List<User>)
  
  fun showFailureLoadingList(e: Throwable)
}