package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User

class FriendsInteractor(
  private val friendsRepository: FriendsRepository,
) {
  
  suspend fun getListByType(type: FriendsType): List<User> {
    return friendsRepository.getListByType(type)
  }
  
  suspend fun removeFriend(user: User) {
    friendsRepository.removeFriend(user)
  }
  
  suspend fun cancelMyRequest(user: User) {
    friendsRepository.cancelMyRequest(user)
  }
  
  suspend fun dismissRequest(user: User) {
    friendsRepository.dismissRequest(user)
  }
  
  suspend fun acceptRequest(user: User) {
    friendsRepository.acceptRequest(user)
  }
}