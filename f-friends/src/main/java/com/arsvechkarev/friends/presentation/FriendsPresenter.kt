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
  
  fun loadList(type: FriendsType = currentFriendsType) {
    currentFriendsType = type
    if (!interactor.hasCacheFor(type)) {
      viewState.showLoading(type)
    }
    loadListOfType(type, true)
  }
  
  fun performFiltering(text: String) {
    coroutine {
      val list = interactor.getFromCache(currentFriendsType)?.filter { user ->
        user.username.startsWith(text, ignoreCase = true)
      }
      if (list != null) {
        viewState.showSearchResult(list)
      }
    }
  }
  
  fun showCurrentList() {
    val list = interactor.getFromCache(currentFriendsType) ?: return
    viewState.showList(currentFriendsType, list)
  }
  
  fun onRefreshPulled() {
    loadListOfType(currentFriendsType, false)
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
        if (!interactor.hasCacheFor(type) || !allowUseCache) {
          delay(MIN_NETWORK_DELAY)
        }
        val data = interactor.getListByType(type, allowUseCache)
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