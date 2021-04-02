package com.arsvechkarev.friends.domain

import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.AcceptRequest
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.CancelMyRequest
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.DismissRequest
import com.arsvechkarev.friends.domain.FriendsPagerScreenAction.RemoveFromFriends
import com.arsvechkarev.friends.domain.FriendsScreensEvent.AcceptedRequest
import com.arsvechkarev.friends.domain.FriendsScreensEvent.OnUserClicked
import com.arsvechkarev.friends.presentation.FriendsScreen
import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMePagerScreen
import core.model.FriendsType
import core.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * This class helps in communication between base screen [FriendsScreen] and other screens in
 * view pager, like [AllFriendsPagerScreen], [MyRequestsPagerScreen] and [RequestsToMePagerScreen].
 */
class FriendsScreensCommunicator {
  
  private val _friendsScreenEvents = MutableSharedFlow<FriendsScreensEvent>()
  private val _friendsPagerScreenActions = MutableSharedFlow<FriendsPagerScreenAction>()
  
  @OptIn(ExperimentalCoroutinesApi::class)
  val friendsScreenEvents: SharedFlow<FriendsScreensEvent>
    get() = _friendsScreenEvents
  
  @OptIn(ExperimentalCoroutinesApi::class)
  val friendsPagerScreenActions: SharedFlow<FriendsPagerScreenAction>
    get() = _friendsPagerScreenActions
  
  suspend fun onUserClicked(friendsType: FriendsType, user: User) {
    _friendsScreenEvents.emit(OnUserClicked(friendsType, user))
  }
  
  suspend fun notifyRequestAccepted() {
    _friendsScreenEvents.emit(AcceptedRequest)
  }
  
  suspend fun sendRemoveFriendAction(user: User) {
    _friendsPagerScreenActions.emit(RemoveFromFriends(user))
  }
  
  suspend fun sendCancelMyRequestAction(user: User) {
    _friendsPagerScreenActions.emit(CancelMyRequest(user))
  }
  
  suspend fun sendAcceptRequestAction(user: User) {
    _friendsPagerScreenActions.emit(AcceptRequest(user))
  }
  
  suspend fun sendDismissRequestAction(user: User) {
    _friendsPagerScreenActions.emit(DismissRequest(user))
  }
}