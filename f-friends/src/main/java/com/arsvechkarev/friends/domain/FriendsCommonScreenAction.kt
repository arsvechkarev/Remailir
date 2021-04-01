package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User

sealed class FriendsCommonScreenAction {
  
  class OnUserClicked(val friendsType: FriendsType, val user: User) : FriendsCommonScreenAction()
}