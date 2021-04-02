package com.arsvechkarev.friends.domain

import core.model.FriendsType
import core.model.User

sealed class FriendsCommonScreenAction {
  
  class OnUserClicked(val friendsType: FriendsType, val user: User) : FriendsCommonScreenAction()
}