package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.model.User

interface UserInfoDatabase {
  
  suspend fun saveUser(username: String, email: String)
  
  suspend fun getFriendsList(thisUserUsername: String): List<User>
}