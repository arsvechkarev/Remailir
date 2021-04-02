package com.arsvechkarev.friends.domain

import core.model.FriendsType
import core.model.User

interface FriendsRepository {
  
  fun getFromCache(type: FriendsType): List<User>?
  
  suspend fun getListByType(type: FriendsType): List<User>
  
  suspend fun removeFriend(user: User)
  
  suspend fun cancelMyRequest(user: User)
  
  suspend fun dismissRequest(user: User)
  
  suspend fun acceptRequest(user: User)
}