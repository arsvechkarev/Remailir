package com.arsvechkarev.friends.presentation

import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.domain.FriendsScreensEvent.OnUserClicked
import com.arsvechkarev.friends.presentation.FriendsPresenter.FriendsScreenState.NONE
import com.arsvechkarev.friends.presentation.FriendsPresenter.FriendsScreenState.SHOWING_ACTION_DIALOG
import com.arsvechkarev.friends.presentation.FriendsPresenter.FriendsScreenState.SHOWING_CONFIRMATION_DIALOG
import core.Dispatchers
import core.model.User
import core.ui.BasePresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import navigation.Router
import javax.inject.Inject

class FriendsPresenter @Inject constructor(
  private val router: Router,
  private val screensCommunicator: FriendsScreensCommunicator,
  dispatchers: Dispatchers,
) : BasePresenter<FriendsView>(dispatchers) {
  
  private var friendsScreenState = NONE
  
  init {
    scope.launch {
      screensCommunicator.friendsScreenEvents
          .filterIsInstance<OnUserClicked>()
          .collect { event ->
            friendsScreenState = SHOWING_ACTION_DIALOG
            viewState.showActionDialog(event.friendsType, event.user)
          }
    }
  }
  
  fun startChatWith(user: User) {
    // TODO (4/6/2021): start chatting
  }
  
  fun onHideDialog() {
    viewState.hideActionDialog()
  }
  
  fun askForFriendRemovingConfirmation(user: User) {
    friendsScreenState = SHOWING_CONFIRMATION_DIALOG
    viewState.showRemovingFriendConfirmationDialog(user)
  }
  
  fun sendRemoveFriendAction(user: User) {
    friendsScreenState = NONE
    viewState.hideActionDialog()
    coroutine {
      screensCommunicator.sendRemoveFriendAction(user)
    }
  }
  
  fun sendCancelMyRequestAction(user: User) {
    friendsScreenState = NONE
    viewState.hideActionDialog()
    coroutine {
      screensCommunicator.sendCancelMyRequestAction(user)
    }
  }
  
  fun sendAcceptRequestAction(user: User) {
    friendsScreenState = NONE
    viewState.hideActionDialog()
    coroutine {
      screensCommunicator.sendAcceptRequestAction(user)
    }
  }
  
  fun sendDismissRequestAction(user: User) {
    friendsScreenState = NONE
    viewState.hideActionDialog()
    coroutine {
      screensCommunicator.sendDismissRequestAction(user)
    }
  }
  
  fun handleBackPress() {
    when (friendsScreenState) {
      NONE -> {
        // Currently, when user clicks 'back', friends screen is cached and not removed, therefore
        // views and presenters are not created from scratch when user returns back to the
        // screen. Change this flag if the screen needs to be removed and created from scratch
        val releaseCurrentScreen = false
        if (releaseCurrentScreen) {
          viewState.onRemovingFromScreen()
        }
        router.goBack(releaseCurrentScreen)
      }
      SHOWING_ACTION_DIALOG -> {
        friendsScreenState = NONE
        viewState.hideActionDialog()
      }
      SHOWING_CONFIRMATION_DIALOG -> {
        friendsScreenState = SHOWING_ACTION_DIALOG
        viewState.hideRemovingFriendConfirmationDialog()
      }
    }
  }
  
  enum class FriendsScreenState {
    NONE, SHOWING_ACTION_DIALOG, SHOWING_CONFIRMATION_DIALOG
  }
}