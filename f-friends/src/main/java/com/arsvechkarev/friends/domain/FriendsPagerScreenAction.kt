package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.User

/**
 * Actions that passed from main friends screens to view pager screens
 */
sealed class FriendsPagerScreenAction {
  
  class RemoveFromFriends(val user: User) : FriendsPagerScreenAction()
  
  class CancelMyRequest(val user: User) : FriendsPagerScreenAction()
  
  class AcceptRequest(val user: User) : FriendsPagerScreenAction()
  
  class DismissRequest(val user: User) : FriendsPagerScreenAction()
}

