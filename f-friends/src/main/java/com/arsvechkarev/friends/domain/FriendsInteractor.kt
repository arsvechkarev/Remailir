package com.arsvechkarev.friends.domain

import core.Dispatchers
import core.model.FriendsType
import core.model.User
import kotlinx.coroutines.withContext

class FriendsInteractor(
  private val friendsRepository: FriendsRepository,
  private val dispatchers: Dispatchers
) {
  
  fun getCachedListByType(type: FriendsType): List<User> {
    return friendsRepository.getFromCache(type) ?: error("Cache of type $type is null")
  }
  
  suspend fun getListByType(type: FriendsType): List<User> = withContext(dispatchers.IO) {
    friendsRepository.getListByType(type)
  }
  
  suspend fun removeFriend(user: User) = withContext(dispatchers.IO) {
    friendsRepository.removeFriend(user)
  }
  
  suspend fun cancelMyRequest(user: User) = withContext(dispatchers.IO) {
    friendsRepository.cancelMyRequest(user)
  }
  
  suspend fun dismissRequest(user: User) = withContext(dispatchers.IO) {
    friendsRepository.dismissRequest(user)
  }
  
  suspend fun acceptRequest(user: User) = withContext(dispatchers.IO) {
    friendsRepository.acceptRequest(user)
  }
}