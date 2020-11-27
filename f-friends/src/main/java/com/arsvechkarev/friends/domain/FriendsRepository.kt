package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.FRIENDS_REQUESTS
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.firebase.database.Database
import com.arsvechkarev.firebase.database.Schema

class FriendsRepository(
  private val thisUsername: String,
  private val schema: Schema,
  private val database: Database
) {
  
  private val cache = HashMap<FriendsType, MutableList<String>>()
  
  fun hasCacheFor(type: FriendsType) = cache.contains(type)
  
  fun getFromCache(friendsType: FriendsType) = cache[friendsType]
  
  suspend fun getListByType(friendsType: FriendsType): List<String> {
    val path = when (friendsType) {
      ALL_FRIENDS -> schema.friendsPath(thisUsername)
      MY_REQUESTS -> schema.friendsRequestsFromMePath(thisUsername)
      FRIENDS_REQUESTS -> schema.friendsRequestsToMePath(thisUsername)
    }
    val list = getList(path)
    cache[friendsType] = list
    return list
  }
  
  suspend fun removeFriend(otherUsername: String) {
    performRemove(
      schema.friendsPath(thisUsername), otherUsername,
      schema.friendsPath(otherUsername), thisUsername
    )
    cache[ALL_FRIENDS]?.remove(otherUsername)
  }
  
  suspend fun cancelMyRequest(otherUsername: String) {
    performRemove(
      schema.friendsRequestsFromMePath(thisUsername), otherUsername,
      schema.friendsRequestsToMePath(otherUsername), thisUsername,
    )
    cache[MY_REQUESTS]?.remove(otherUsername)
  }
  
  suspend fun dismissRequest(otherUsername: String) {
    performRemove(
      schema.friendsRequestsToMePath(thisUsername), otherUsername,
      schema.friendsRequestsFromMePath(otherUsername), thisUsername,
    )
    cache[FRIENDS_REQUESTS]?.remove(otherUsername)
  }
  
  suspend fun acceptRequest(otherUsername: String) {
    val thisFriendsPath = schema.friendsPath(thisUsername)
    val otherUserFriendsPath = schema.friendsPath(otherUsername)
    val thisUserRequestsToMePath = schema.friendsRequestsToMePath(thisUsername)
    val thisUserRequestsFromMePath = schema.friendsRequestsFromMePath(thisUsername)
    val otherUserRequestsToMePath = schema.friendsRequestsToMePath(otherUsername)
    val otherUserRequestsFromMePath = schema.friendsRequestsFromMePath(otherUsername)
    val thisFriends = getList(thisFriendsPath)
    val otherUserFriends = getList(otherUserFriendsPath)
    val requestsToMe = getList(thisUserRequestsToMePath)
    val requestsFromMe = getList(thisUserRequestsFromMePath)
    val requestsToOtherUser = getList(otherUserRequestsToMePath)
    val requestsFromOtherUser = getList(otherUserRequestsFromMePath)
    thisFriends.add(otherUsername)
    otherUserFriends.add(thisUsername)
    requestsToMe.remove(otherUsername)
    requestsFromMe.remove(otherUsername)
    requestsToOtherUser.remove(thisUsername)
    requestsFromOtherUser.remove(thisUsername)
    val map = mutableMapOf<String, Any>(
      thisFriendsPath to thisFriends,
      otherUserFriendsPath to otherUserFriends,
      thisUserRequestsToMePath to requestsToMe,
      thisUserRequestsFromMePath to requestsFromMe,
      otherUserRequestsToMePath to requestsToOtherUser,
      otherUserRequestsFromMePath to requestsFromOtherUser,
    )
    for ((k, v) in map) {
      if ((v as MutableList<String>).isEmpty()) {
        map[k] = ""
      }
    }
    database.setValues(map)
    cache[ALL_FRIENDS]?.add(otherUsername)
    cache[MY_REQUESTS]?.remove(otherUsername)
    cache[FRIENDS_REQUESTS]?.remove(otherUsername)
  }
  
  private suspend fun performRemove(
    path1: String,
    value1: String,
    path2: String,
    value2: String,
  ) {
    val list1 = getList(path1)
    val list2 = getList(path2)
    list1.remove(value1)
    list2.remove(value2)
    val v1: Any = if (list1.isNotEmpty()) list1 else ""
    val v2: Any = if (list2.isNotEmpty()) list2 else ""
    database.setValues(mapOf(path1 to v1, path2 to v2))
  }
  
  private suspend fun getList(path: String) = database.getList(path)
}