package com.arsvechkarev.friends.domain

import firebase.database.FirebaseDatabase
import core.StringToUserMapper
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import core.model.FriendsType
import core.model.FriendsType.ALL_FRIENDS
import core.model.FriendsType.MY_REQUESTS
import core.model.FriendsType.REQUESTS_TO_ME
import core.model.User
import javax.inject.Inject

class FriendsRepositoryImpl @Inject constructor(
  private val thisUser: User,
  private val schema: UsersDatabaseSchema,
  private val database: FirebaseDatabase,
  private val usersActions: UsersActions,
  private val userMapper: StringToUserMapper
) : FriendsRepository {
  
  private val cache = HashMap<FriendsType, MutableList<User>>()
  
  override fun getFromCache(type: FriendsType): List<User>? = cache[type]
  
  override suspend fun getListByType(type: FriendsType): List<User> {
    val path = when (type) {
      ALL_FRIENDS -> schema.friendsPath(thisUser)
      MY_REQUESTS -> schema.friendsRequestsFromUserPath(thisUser)
      REQUESTS_TO_ME -> schema.friendsRequestsToUserPath(thisUser)
    }
    val destination = ArrayList<User>()
    return database.getList(path).mapTo(destination, userMapper::map).also { cache[type] = it }
  }
  
  override suspend fun removeFriend(user: User) {
    performUserRemove(
      schema.friendsPath(thisUser), user,
      schema.friendsPath(user), thisUser
    )
    cache[ALL_FRIENDS]?.remove(user)
  }
  
  override suspend fun cancelMyRequest(user: User) {
    performUserRemove(
      schema.friendsRequestsFromUserPath(thisUser), user,
      schema.friendsRequestsToUserPath(user), thisUser,
    )
    cache[MY_REQUESTS]?.remove(user)
  }
  
  override suspend fun dismissRequest(user: User) {
    performUserRemove(
      schema.friendsRequestsToUserPath(thisUser), user,
      schema.friendsRequestsFromUserPath(user), thisUser,
    )
    cache[REQUESTS_TO_ME]?.remove(user)
  }
  
  override suspend fun acceptRequest(user: User) {
    val thisFriendsPath = schema.friendsPath(thisUser)
    val otherUserFriendsPath = schema.friendsPath(user)
    val thisUserRequestsToMePath = schema.friendsRequestsToUserPath(thisUser)
    val thisUserRequestsFromMePath = schema.friendsRequestsFromUserPath(thisUser)
    val otherUserRequestsToMePath = schema.friendsRequestsToUserPath(user)
    val otherUserRequestsFromMePath = schema.friendsRequestsFromUserPath(user)
    val thisUserFriends = database.getList(thisFriendsPath)
    val otherUserFriends = database.getList(otherUserFriendsPath)
    val requestsToMe = database.getList(thisUserRequestsToMePath)
    val requestsFromMe = database.getList(thisUserRequestsFromMePath)
    val requestsToOtherUser = database.getList(otherUserRequestsToMePath)
    val requestsFromOtherUser = database.getList(otherUserRequestsFromMePath)
    val map = mutableMapOf<String, Any>(
      thisFriendsPath to usersActions.add(thisUserFriends, user),
      otherUserFriendsPath to usersActions.add(otherUserFriends, thisUser),
      thisUserRequestsToMePath to usersActions.remove(requestsToMe, user),
      thisUserRequestsFromMePath to usersActions.remove(requestsFromMe, user),
      otherUserRequestsToMePath to usersActions.remove(requestsToOtherUser, thisUser),
      otherUserRequestsFromMePath to usersActions.remove(requestsFromOtherUser, thisUser),
    )
    database.setValues(map)
    cache[ALL_FRIENDS]?.add(user)
    cache[MY_REQUESTS]?.remove(user)
    cache[REQUESTS_TO_ME]?.remove(user)
  }
  
  /**
   * Removes [user1] from list by path [pathToList1] and [user2] from list by path [pathToList2]
   */
  private suspend fun performUserRemove(
    pathToList1: String,
    user1: User,
    pathToList2: String,
    user2: User,
  ) {
    val list1 = database.getList(pathToList1)
    val list2 = database.getList(pathToList2)
    usersActions.remove(list1, user1)
    usersActions.remove(list2, user2)
    val v1: Any = if (list1.isNotEmpty()) list1 else ""
    val v2: Any = if (list2.isNotEmpty()) list2 else ""
    database.setValues(mapOf(pathToList1 to v1, pathToList2 to v2))
  }
}