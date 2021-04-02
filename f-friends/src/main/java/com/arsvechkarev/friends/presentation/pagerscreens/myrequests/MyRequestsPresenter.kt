package com.arsvechkarev.friends.presentation.pagerscreens.myrequests

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.CancelMyRequest
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import core.Dispatchers
import core.model.FriendsType
import core.model.User
import core.ui.BasePresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyRequestsPresenter @Inject constructor(
  private val interactor: FriendsInteractor,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers
) : BasePresenter<MyRequestsView>(dispatchers) {
  
  fun startLoadingMyRequests() {
    coroutine {
      val friends = interactor.getListByType(FriendsType.MY_REQUESTS)
      if (friends.isEmpty()) {
        viewState.showListIsEmpty()
      } else {
        viewState.showLoadedList(friends)
      }
    }
  }
  
  fun startListeningToMyRequestsChanges() {
    scope.launch {
      screensCommunicator.friendsPagerScreenActions
          .filterIsInstance<CancelMyRequest>()
          .collect { performCancelMyRequest(it.user) }
    }
  }
  
  fun onUserClicked(user: User) {
    coroutine {
      screensCommunicator.onUserClicked(FriendsType.MY_REQUESTS, user)
    }
  }
  
  private fun performCancelMyRequest(user: User) {
    viewState.showLoadingCancelMyRequest(user)
    coroutine {
      interactor.cancelMyRequest(user)
      viewState.showSuccessCancelMyRequest(user)
    }
  }
}