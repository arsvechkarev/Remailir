package com.arsvechkarev.friends.presentation.pagerscreens.requeststome

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.AcceptRequest
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.DismissRequest
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RequestsToMePresenter(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<RequestsToMeView>(dispatchers) {
  
  fun startLoadingRequestsToMe() {
    coroutine {
      viewState.showLoadingList()
      val friends = interactor.getListByType(FriendsType.REQUESTS_TO_ME)
      if (friends.isEmpty()) {
        viewState.showListIsEmpty()
      } else {
        viewState.showLoadedList(friends)
      }
    }
  }
  
  fun startListeningToRequestsToMeChanges() {
    scope.launch {
      screensCommunicator.friendsPagerScreenActions
          .collect { action ->
            if (action is AcceptRequest) {
              performAcceptRequest(action.user)
            }
            if (action is DismissRequest) {
              performDismissRequest(action.user)
            }
          }
    }
  }
  
  fun onUserClicked(user: User) {
    coroutine {
      screensCommunicator.onUserClicked(FriendsType.REQUESTS_TO_ME, user)
    }
  }
  
  private fun performAcceptRequest(user: User) {
    coroutine {
      viewState.showLoadingAcceptingRequest(user)
      interactor.acceptRequest(user)
      screensCommunicator.notifyRequestAccepted(user)
      viewState.showSuccessAcceptingRequest(user)
    }
  }
  
  private fun performDismissRequest(user: User) {
    coroutine {
      viewState.showLoadingDismissingRequest(user)
      interactor.dismissRequest(user)
      viewState.showSuccessDismissingRequest(user)
    }
  }
}