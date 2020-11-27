package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.Data
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.model.toUsersList

class FriendsInteractor(
  private val repository: FriendsRepository
) {
  
  suspend fun getListByType(
    friendsType: FriendsType,
    allowUseCache: Boolean,
  ): Data<List<User>> {
    if (allowUseCache) {
      val cachedList = repository.getFromCache(friendsType)
      if (cachedList != null) {
        return Data(cachedList.toUsersList(), isFromCache = true)
      }
    }
    val list = repository.getListByType(friendsType).toUsersList()
    return Data(list, isFromCache = false)
  }
  
  fun hasCacheFor(type: FriendsType) = repository.hasCacheFor(type)
  
  fun getFromCache(friendsType: FriendsType): List<User>? {
    return repository.getFromCache(friendsType)?.toUsersList()
  }
  
  suspend fun removeFriend(otherUsername: String) {
    repository.removeFriend(otherUsername)
  }
  
  suspend fun cancelMyRequest(otherUsername: String) {
    repository.cancelMyRequest(otherUsername)
  }
  
  suspend fun dismissRequest(otherUsername: String) {
    repository.dismissRequest(otherUsername)
  }
  
  suspend fun acceptRequest(otherUsername: String) {
    repository.acceptRequest(otherUsername)
  }
}