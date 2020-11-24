package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.Data
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.auth.Authenticator
import com.arsvechkarev.firebase.database.UserInfoDatabase
import kotlinx.coroutines.withContext

class FriendsRepository(
  private val authenticator: Authenticator,
  private val database: UserInfoDatabase,
  private val dispatchers: Dispatchers
) {
  
  private val cache = HashMap<FriendsType, List<User>>()
  
  suspend fun loadListOfType(
    type: FriendsType,
    allowUseCache: Boolean
  ): Data<List<User>> = withContext(dispatchers.IO) lb@{
    if (allowUseCache && cache.containsKey(type)) {
      return@lb Data(cache.getValue(type), true)
    }
    val list = database.getListOfType(authenticator.getUsername(), type)
    cache[type] = list
    return@lb Data(list, false)
  }
  
  fun hasCacheFor(type: FriendsType): Boolean {
    return cache.containsKey(type)
  }
}