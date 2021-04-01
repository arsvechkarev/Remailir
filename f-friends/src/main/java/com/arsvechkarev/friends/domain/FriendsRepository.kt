package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User

interface FriendsRepository {
  
  fun getFromCache(type: FriendsType): List<User>?
  
  suspend fun getListByType(type: FriendsType): List<User>
  
  suspend fun removeFriend(user: User)
  
  suspend fun cancelMyRequest(user: User)
  
  suspend fun dismissRequest(user: User)
  
  suspend fun acceptRequest(user: User)
}