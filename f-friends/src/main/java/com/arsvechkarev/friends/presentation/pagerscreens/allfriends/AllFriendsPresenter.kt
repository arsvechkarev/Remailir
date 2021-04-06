package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.RemoveFromFriends
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.domain.FriendsScreensEvent.AcceptedRequest
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState.EMPTY
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState.FAILURE
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState.LOADING
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState.REFRESHING
import com.arsvechkarev.friends.presentation.pagerscreens.PagerScreenState.SUCCESS
import core.Dispatchers
import core.model.FriendsType.ALL_FRIENDS
import core.model.User
import core.ui.BasePresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllFriendsPresenter @Inject constructor(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<AllFriendsView>(dispatchers) {
  
  private var screenState = PagerScreenState.NONE
  
  override fun onFirstViewAttach() {
    screenState = LOADING
    startLoadingAllFriends()
    startListeningToFriendsActionsAndEvents()
  }
  
  fun onRefreshPulled() {
    screenState = LOADING
    startLoadingAllFriends(notifyLoading = false)
  }
  
  fun allowPulling(): Boolean {
    return screenState == EMPTY || screenState == SUCCESS
  }
  
  fun onUserClicked(user: User) {
    if (screenState != SUCCESS) return
    coroutine {
      screensCommunicator.onUserClicked(ALL_FRIENDS, user)
    }
  }
  
  fun onRetryLoadingAllDataClicked() {
    if (screenState != FAILURE) return
    screenState = LOADING
    startLoadingAllFriends()
  }
  
  fun onRetryRemovingFromFriendsClicked(user: User) {
    screenState = LOADING
    performRemoveFromFriends(user)
  }
  
  fun onHideSnackbarClicked() {
    viewState.hideLayoutRemovingFromFriends()
  }
  
  private fun startLoadingAllFriends(notifyLoading: Boolean = true) {
    screenState = LOADING
    if (notifyLoading) {
      viewState.showLoadingList()
    }
    coroutine {
      try {
        fakeNetworkDelay()
        val friends = interactor.getListByType(ALL_FRIENDS)
        if (friends.isEmpty()) {
          screenState = EMPTY
          viewState.showListIsEmpty()
        } else {
          screenState = SUCCESS
          viewState.showLoadedList(friends)
        }
      } catch (e: Throwable) {
        screenState = FAILURE
        viewState.showFailureLoadingList(e)
      }
    }
  }
  
  private fun startListeningToFriendsActionsAndEvents() {
    scope.launch {
      screensCommunicator.friendsScreenEvents
          .filterIsInstance<AcceptedRequest>()
          .map { interactor.getCachedListByType(ALL_FRIENDS) }
          .collect(viewState::showNewFriendAdded)
    }
    scope.launch {
      screensCommunicator.friendsPagerScreenActions
          .filterIsInstance<RemoveFromFriends>()
          .collect { performRemoveFromFriends(it.user) }
    }
  }
  
  private fun performRemoveFromFriends(user: User) {
    viewState.showLoadingRemovingFromFriends(user)
    coroutine {
      try {
        fakeNetworkDelay()
        interactor.removeFriend(user)
        val updatedFriendsList = interactor.getCachedListByType(ALL_FRIENDS)
        viewState.showSuccessRemovingFromFriends(user)
        if (updatedFriendsList.isNotEmpty()) {
          viewState.showListChanged(updatedFriendsList)
        } else {
          viewState.showListIsEmpty()
        }
      } catch (e: Throwable) {
        viewState.showFailureRemovingFromFriends(user, e)
      }
    }
  }
}