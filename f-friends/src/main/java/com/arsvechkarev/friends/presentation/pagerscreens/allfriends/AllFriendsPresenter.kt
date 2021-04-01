package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.RemoveFromFriends
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.domain.FriendsScreensEvent.AcceptedRequest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class AllFriendsPresenter(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<AllFriendsView>(dispatchers) {
  
  fun startLoadingAllFriends() {
    viewState.showLoadingList()
    coroutine {
      val friends = interactor.getListByType(FriendsType.ALL_FRIENDS)
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
          .filterIsInstance<AcceptedRequest>()
          .collect { event -> viewState.showFriendAdded(event.user) }
    }
    scope.launch {
      screensCommunicator.friendsPagerScreenActions
          .filterIsInstance<RemoveFromFriends>()
          .collect { action -> performRemoveFromFriends(action.user) }
    }
  }
  
  fun onUserClicked(user: User) {
    coroutine {
      screensCommunicator.onUserClicked(FriendsType.ALL_FRIENDS, user)
    }
  }
  
  private fun performRemoveFromFriends(user: User) {
    viewState.showLoadingRemovingFromFriends(user)
    coroutine {
      interactor.removeFriend(user)
      viewState.showSuccessRemovingFromFriends(user)
    }
  }
}