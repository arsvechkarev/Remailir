package com.arsvechkarev.friends.domain

import core.model.FriendsType
import core.model.User

/**
 * Events that passed between friend screens
 */
sealed class FriendsScreensEvent {
  
  class OnUserClicked(val friendsType: FriendsType, val user: User) : FriendsScreensEvent()
  
  class AcceptedRequest(val user: User) : FriendsScreensEvent()
}