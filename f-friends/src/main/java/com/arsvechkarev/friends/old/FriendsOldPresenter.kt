package com.arsvechkarev.friends.old

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.REQUESTS_TO_ME
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.model.UserAction
import com.arsvechkarev.core.model.UserAction.ADDING_TO_FRIENDS
import com.arsvechkarev.core.model.UserAction.CANCELING_MY_REQUEST
import com.arsvechkarev.core.model.UserAction.DISMISSING_REQUEST
import com.arsvechkarev.core.model.UserAction.REMOVE_FROM_FRIENDS
import kotlinx.coroutines.delay

class FriendsOldPresenter(
  private val oldInteractor: FriendsOldInteractor,
  dispatchers: Dispatchers
) : BasePresenter<FriendsOldView>(dispatchers) {
  
  private var currentFriendsType = ALL_FRIENDS
  
  fun loadList(
    type: FriendsType = currentFriendsType,
    allowUseCache: Boolean = true
  ) {
    currentFriendsType = type
    if (!oldInteractor.hasCacheFor(type)) {
      viewState.showLoading(type)
    }
    loadListOfType(type, allowUseCache)
  }
  
  fun performFiltering(text: String) {
    coroutine {
      val list = oldInteractor.getFromCache(currentFriendsType)?.filter { user ->
        user.username.startsWith(text, ignoreCase = true)
      }
      if (list != null) {
        viewState.showSearchResult(list)
      }
    }
  }
  
  fun showCurrentListIfNotEmpty() {
    val list = oldInteractor.getFromCache(currentFriendsType) ?: return
    if (list.isNotEmpty()) {
      viewState.showList(currentFriendsType, list)
    }
  }
  
  fun onRefreshPulled() {
    loadListOfType(currentFriendsType, false)
  }
  
  fun askForFriendRemovingConfirmation(user: User) {
    viewState.showRemovingFriendConfirmationDialog(user)
  }
  
  fun onUserClicked(user: User) {
    viewState.showUserDialog(currentFriendsType, user)
  }
  
  fun performAction(userAction: UserAction, user: User) {
    coroutine {
      viewState.showLoadingUserAction(userAction)
      try {
        delay(MIN_NETWORK_DELAY)
        when (userAction) {
          REMOVE_FROM_FRIENDS -> oldInteractor.removeFriend(user)
          CANCELING_MY_REQUEST -> oldInteractor.cancelMyRequest(user)
          ADDING_TO_FRIENDS -> oldInteractor.acceptRequest(user)
          DISMISSING_REQUEST -> oldInteractor.dismissRequest(user)
        }
        val friendsType = when (userAction) {
          REMOVE_FROM_FRIENDS -> ALL_FRIENDS
          CANCELING_MY_REQUEST -> MY_REQUESTS
          ADDING_TO_FRIENDS, DISMISSING_REQUEST -> REQUESTS_TO_ME
        }
        val list = oldInteractor.getFromCache(friendsType)!!
        if (list.isEmpty()) {
          viewState.showCompletedUserAction(friendsType)
        } else {
          viewState.showCompletedUserAction(friendsType, list)
        }
      } catch (e: Throwable) {
        viewState.showUserActionFailure(userAction, user, e)
      }
    }
  }
  
  private fun loadListOfType(type: FriendsType, allowUseCache: Boolean) {
    coroutine {
      try {
        if (!oldInteractor.hasCacheFor(type) || !allowUseCache) {
          delay(MIN_NETWORK_DELAY)
        }
        val data = oldInteractor.getListByType(type, allowUseCache)
        if (data.isFromCache) {
          if (data.value.isEmpty()) {
            viewState.showSwitchedToEmptyView(type)
          } else {
            viewState.showSwitchedToList(type, data.value)
          }
        } else {
          if (data.value.isEmpty()) {
            viewState.showNoData(type)
          } else {
            viewState.showList(type, data.value)
          }
        }
      } catch (e: Throwable) {
        viewState.showFailure(e)
      }
    }
  }
}