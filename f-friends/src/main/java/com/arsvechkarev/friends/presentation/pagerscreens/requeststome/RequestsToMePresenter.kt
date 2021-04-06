package com.arsvechkarev.friends.presentation.pagerscreens.requeststome

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.AcceptRequest
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.DismissRequest
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import core.Dispatchers
import core.model.FriendsType.REQUESTS_TO_ME
import core.model.User
import core.ui.BasePresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RequestsToMePresenter @Inject constructor(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<RequestsToMeView>(dispatchers) {
  
  fun startLoadingRequestsToMe() {
    viewState.showLoadingList()
    coroutine {
      fakeNetworkDelay()
      val friends = interactor.getListByType(REQUESTS_TO_ME)
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
      screensCommunicator.onUserClicked(REQUESTS_TO_ME, user)
    }
  }
  
  private fun performAcceptRequest(user: User) {
    coroutine {
      viewState.showLoadingAcceptingRequest(user)
      interactor.acceptRequest(user)
      val updatedRequestToMe = interactor.getCachedListByType(REQUESTS_TO_ME)
      screensCommunicator.notifyRequestAccepted()
      viewState.showSuccessAcceptingRequest(user)
      notifyListChange(updatedRequestToMe)
    }
  }
  
  private fun performDismissRequest(user: User) {
    coroutine {
      viewState.showLoadingDismissingRequest(user)
      interactor.dismissRequest(user)
      val updatedRequestToMe = interactor.getCachedListByType(REQUESTS_TO_ME)
      viewState.showSuccessDismissingRequest(user)
      notifyListChange(updatedRequestToMe)
    }
  }
  
  private fun notifyListChange(updatedRequestToMe: List<User>) {
    if (updatedRequestToMe.isNotEmpty()) {
      viewState.showListChanged(updatedRequestToMe)
    } else {
      viewState.showListIsEmpty()
    }
  }
}