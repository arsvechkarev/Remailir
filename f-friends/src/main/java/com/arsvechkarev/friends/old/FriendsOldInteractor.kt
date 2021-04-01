package com.arsvechkarev.friends.old

import com.arsvechkarev.core.model.Data
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import com.arsvechkarev.friends.domain.FriendsRepositoryImpl

class FriendsOldInteractor(
  private val repository: FriendsRepositoryImpl
) {
  
  suspend fun getListByType(
    friendsType: FriendsType,
    allowUseCache: Boolean,
  ): Data<List<User>> {
    val list = repository.getListByType(friendsType)
    return Data(list, isFromCache = false)
  }
  
  fun hasCacheFor(type: FriendsType) = false
  
  fun getFromCache(friendsType: FriendsType): List<User>? {
    return null
  }
  
  suspend fun removeFriend(otherUser: User) {
    repository.removeFriend(otherUser)
  }
  
  suspend fun cancelMyRequest(otherUser: User) {
    repository.cancelMyRequest(otherUser)
  }
  
  suspend fun dismissRequest(otherUser: User) {
    repository.dismissRequest(otherUser)
  }
  
  suspend fun acceptRequest(otherUser: User) {
    repository.acceptRequest(otherUser)
  }
}