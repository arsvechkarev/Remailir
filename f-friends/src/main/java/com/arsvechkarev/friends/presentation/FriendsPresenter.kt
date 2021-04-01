package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.domain.FriendsScreensEvent.OnUserClicked
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class FriendsPresenter(
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers,
) : BasePresenter<FriendsView>(dispatchers) {
  
  init {
    scope.launch {
      screensCommunicator.friendsScreenEvents
          .filterIsInstance<OnUserClicked>()
          .collect { event -> viewState.showOnUserClicked(event.friendsType, event.user) }
    }
  }
  
  fun askForFriendRemovingConfirmation(user: User) {
    viewState.showRemovingFriendConfirmationDialog(user)
  }
  
  fun sendRemoveFriendAction(user: User) {
    viewState.hideDialog()
    coroutine {
      screensCommunicator.sendRemoveFriendAction(user)
    }
  }
  
  fun sendCancelMyRequestAction(user: User) {
    viewState.hideDialog()
    coroutine {
      screensCommunicator.sendCancelMyRequestAction(user)
    }
  }
  
  fun sendAcceptRequestAction(user: User) {
    viewState.hideDialog()
    coroutine {
      screensCommunicator.sendAcceptRequestAction(user)
    }
  }
  
  fun sendDismissRequestAction(user: User) {
    viewState.hideDialog()
    coroutine {
      screensCommunicator.sendDismissRequestAction(user)
    }
  }
}