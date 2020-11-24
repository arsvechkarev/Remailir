package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User

interface UserInfoDatabase {
  
  suspend fun saveUser(username: String, email: String)
  
  suspend fun getListOfType(thisUserUsername: String, type: FriendsType): List<User>
}