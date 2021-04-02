package com.arsvechkarev.friends.old

import core.model.FriendsType
import core.model.User
import core.model.UserAction
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FriendsOldView : MvpView {
  
  fun showLoading(type: FriendsType)
  
  fun showNoData(type: FriendsType)
  
  fun showList(type: FriendsType, list: List<User>)
  
  fun showSearchResult(list: List<User>)
  
  fun showUserDialog(friendsType: FriendsType, user: User)
  
  fun showRemovingFriendConfirmationDialog(user: User)
  
  fun showSwitchedToList(type: FriendsType, list: List<User>)
  
  fun showSwitchedToEmptyView(type: FriendsType)
  
  fun showFailure(e: Throwable)
  
  fun showLoadingUserAction(userAction: UserAction)
  
  fun showCompletedUserAction(type: FriendsType)
  
  fun showCompletedUserAction(type: FriendsType, list: List<User>)
  
  fun showUserActionFailure(userAction: UserAction, user: User, e: Throwable)
}