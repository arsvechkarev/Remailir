package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.FRIENDS_REQUESTS
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.model.UserAction
import com.arsvechkarev.core.model.UserAction.ADDING_TO_FRIENDS
import com.arsvechkarev.core.model.UserAction.CANCELING_MY_REQUEST
import com.arsvechkarev.core.model.UserAction.DISMISSING_REQUEST
import com.arsvechkarev.core.model.UserAction.REMOVE_FROM_FRIENDS
import com.arsvechkarev.friends.domain.FriendsInteractor
import kotlinx.coroutines.delay

class FriendsPresenter(
  private val interactor: FriendsInteractor,
  dispatchers: Dispatchers
) : BasePresenter<FriendsView>(dispatchers) {
  
  private var currentFriendsType = ALL_FRIENDS
  
  fun loadListOf(type: FriendsType) {
    currentFriendsType = type
    if (!interactor.hasCacheFor(type)) {
      updateView { showLoading(type) }
    }
    loadListOfType(type, true)
  }
  
  fun onRefreshPulled() {
    loadListOfType(currentFriendsType, false)
  }
  
  fun onUserClicked(user: User) {
    updateView { showUserDialog(currentFriendsType, user) }
  }
  
  fun performAction(userAction: UserAction, user: User) {
    coroutine {
      updateView { showLoadingUserAction(userAction) }
      try {
        delay(MIN_NETWORK_DELAY)
        when (userAction) {
          REMOVE_FROM_FRIENDS -> interactor.removeFriend(user.username)
          CANCELING_MY_REQUEST -> interactor.cancelMyRequest(user.username)
          ADDING_TO_FRIENDS -> interactor.acceptRequest(user.username)
          DISMISSING_REQUEST -> interactor.dismissRequest(user.username)
        }
        val friendsType = when (userAction) {
          REMOVE_FROM_FRIENDS -> ALL_FRIENDS
          CANCELING_MY_REQUEST -> MY_REQUESTS
          ADDING_TO_FRIENDS, DISMISSING_REQUEST -> FRIENDS_REQUESTS
        }
        val list = interactor.getFromCache(friendsType)!!
        if (list.isEmpty()) {
          updateView { showCompletedUserAction(friendsType) }
        } else {
          updateView { showCompletedUserAction(friendsType, list) }
        }
      } catch (e: Throwable) {
        updateView { showUserActionFailure(userAction, user, e) }
      }
    }
  }
  
  private fun loadListOfType(type: FriendsType, allowUseCache: Boolean) {
    coroutine {
      try {
        if (!interactor.hasCacheFor(type) || !allowUseCache) {
          delay(MIN_NETWORK_DELAY)
        }
        val data = interactor.getListByType(type, allowUseCache)
        if (data.isFromCache) {
          if (data.value.isEmpty()) {
            updateView { showSwitchedToEmptyView(type) }
          } else {
            updateView { showSwitchedToList(type, data.value) }
          }
        } else {
          if (data.value.isEmpty()) {
            updateView { showNoData(type) }
          } else {
            updateView { showList(type, data.value) }
          }
        }
      } catch (e: Throwable) {
        updateView { showFailure(e) }
      }
    }
  }
}