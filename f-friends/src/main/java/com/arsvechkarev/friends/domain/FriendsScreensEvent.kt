package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User

/**
 * Events that passed between friend screens
 */
sealed class FriendsScreensEvent {
  
  class OnUserClicked(val friendsType: FriendsType, val user: User) : FriendsScreensEvent()
  
  class AcceptedRequest(val user: User) : FriendsScreensEvent()
}