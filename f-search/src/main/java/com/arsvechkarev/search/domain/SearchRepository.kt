package com.arsvechkarev.search.domain

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.database.Database
import com.arsvechkarev.firebase.database.DatabaseSchema
import kotlinx.coroutines.withContext

class SearchRepository(
  private val thisUserUsername: String,
  private val schema: DatabaseSchema,
  private val database: Database,
  private val dispatchers: Dispatchers
) {
  
  private var cache: List<User>? = null
  
  suspend fun getUsersList(
    allowUseCache: Boolean
  ): List<User> = withContext(dispatchers.IO) {
    if (allowUseCache) {
      if (cache != null) {
        return@withContext cache!!
      }
    }
    cache = database.getList(schema.allUsersPath)
        .filter { it != thisUserUsername }
        .map { User(it) }
    return@withContext cache!!
  }
  
  suspend fun sendFriendRequest(username: String): RequestResult {
    val friendsList = getList(schema.friendsPath(thisUserUsername))
    if (friendsList.contains(username)) {
      return RequestResult.ERROR_ALREADY_FRIENDS
    }
    val requestFromMe = getList(schema.friendsRequestsFromMePath(thisUserUsername))
    if (requestFromMe.contains(username)) {
      return RequestResult.ERROR_REQUEST_ALREADY_SENT
    }
    val requestsToMe = getList(schema.friendsRequestsToMePath(thisUserUsername))
    if (requestsToMe.contains(username)) {
      return RequestResult.ERROR_THIS_USER_ALREADY_HAS_REQUEST
    }
    addValues(
      schema.friendsRequestsFromMePath(thisUserUsername), username,
      schema.friendsRequestsToMePath(username), thisUserUsername,
    )
    return RequestResult.SENT
  }
  
  private suspend fun addValues(
    path1: String,
    value1: String,
    path2: String,
    value2: String,
  ) {
    val list1 = getList(path1)
    val list2 = getList(path2)
    list1.add(value1)
    list2.add(value2)
    database.setValues(mapOf(path1 to list1, path2 to list2))
  }
  
  private suspend fun getList(path: String) = database.getList(path)
}