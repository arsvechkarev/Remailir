package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.RemoveFromFriends
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.domain.FriendsScreensEvent.AcceptedRequest
import core.Dispatchers
import core.model.FriendsType.ALL_FRIENDS
import core.model.User
import core.ui.BasePresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllFriendsPresenter @Inject constructor(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<AllFriendsView>(dispatchers) {
  
  fun startLoadingAllFriends() {
    viewState.showLoadingList()
    coroutine {
      val friends = interactor.getListByType(ALL_FRIENDS)
      if (friends.isEmpty()) {
        viewState.showListIsEmpty()
      } else {
        viewState.showLoadedList(friends)
      }
    }
  }
  
  fun startListeningToFriendsActionsAndEvents() {
    scope.launch {
      screensCommunicator.friendsScreenEvents
          .onEach { println() }
          .filterIsInstance<AcceptedRequest>()
          .map { interactor.getCachedListByType(ALL_FRIENDS) }
          .collect(viewState::showNewFriendAdded)
    }
    scope.launch {
      screensCommunicator.friendsPagerScreenActions
          .onEach { println() }
          .filterIsInstance<RemoveFromFriends>()
          .collect { performRemoveFromFriends(it.user) }
    }
  }
  
  fun onUserClicked(user: User) {
    coroutine {
      screensCommunicator.onUserClicked(ALL_FRIENDS, user)
    }
  }
  
  private fun performRemoveFromFriends(user: User) {
    viewState.showLoadingRemovingFromFriends(user)
    coroutine {
      interactor.removeFriend(user)
      val updatedFriendsList = interactor.getCachedListByType(ALL_FRIENDS)
      viewState.showSuccessRemovingFromFriends(user)
      if (updatedFriendsList.isNotEmpty()) {
        viewState.showListChanged(updatedFriendsList)
      } else {
        viewState.showListIsEmpty()
      }
    }
  }
}