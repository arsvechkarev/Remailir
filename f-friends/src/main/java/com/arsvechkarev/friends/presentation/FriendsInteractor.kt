package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.auth.Authenticator
import com.arsvechkarev.firebase.database.UserInfoDatabase

class FriendsInteractor(
  private val authenticator: Authenticator,
  private val database: UserInfoDatabase
) {
  
  suspend fun loadFriendsList(): List<User> {
    return database.getFriendsList(authenticator.getUsername())
  }
}