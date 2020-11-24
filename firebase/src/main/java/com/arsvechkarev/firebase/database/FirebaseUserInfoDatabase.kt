package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.assertThat
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.extenstions.waitForSingleValueEvent
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.FRIENDS_REQUESTS
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.UsernameAlreadyExistsException
import com.arsvechkarev.firebase.database.Schema.friend_requests_from_me
import com.arsvechkarev.firebase.database.Schema.friend_requests_to_me
import com.arsvechkarev.firebase.database.Schema.friends
import com.arsvechkarev.firebase.database.Schema.usernames
import com.arsvechkarev.firebase.database.Schema.users
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class FirebaseUserInfoDatabase(
  private val dispatchers: Dispatchers
) : UserInfoDatabase {
  
  private val reference = FirebaseDatabase.getInstance().reference
  
  override suspend fun saveUser(
    username: String,
    email: String
  ): Unit = withContext(dispatchers.IO) {
    val usernamesList = tryUpdateUsernames(username)
    val savingList = async(dispatchers.IO) {
      reference.child(usernames)
          .setValue(usernamesList)
          .await()
    }
    val savingEmail = updateUserDataAsync(username, Schema.email, email)
    val savingFriendRequestsFromMe = updateUserDataAsync(username, friend_requests_from_me, "")
    val savingFriendRequestsToMe = updateUserDataAsync(username, friend_requests_to_me, "")
    val savingFriends = updateUserDataAsync(username, friends, "")
    savingList.await()
    savingEmail.await()
    savingFriendRequestsFromMe.await()
    savingFriendRequestsToMe.await()
    savingFriends.await()
  }
  
  override suspend fun getListOfType(
    thisUserUsername: String, type: FriendsType
  ): List<User> = withContext(dispatchers.IO) {
    val secondChild = when (type) {
      ALL_FRIENDS -> friends
      MY_REQUESTS -> friend_requests_from_me
      FRIENDS_REQUESTS -> friend_requests_to_me
    }
    val snapshot = reference.child(users).child(thisUserUsername)
        .child(secondChild).waitForSingleValueEvent()
    if (!snapshot.exists() || !snapshot.hasChildren()) {
      return@withContext emptyList()
    }
    val list = ArrayList<User>()
    for (snap in snapshot.children) {
      val username = snap.value as? String
      assertThat(!username.isNullOrBlank())
      list.add(User(username))
    }
    return@withContext list
  }
  
  private suspend fun tryUpdateUsernames(username: String): ArrayList<String> {
    val snapshot = reference.child(usernames).waitForSingleValueEvent()
    val usernamesList = ArrayList<String>()
    for (child in snapshot.children) {
      val remoteUsername = child.value as String
      if (remoteUsername == username) {
        throw UsernameAlreadyExistsException()
      }
      usernamesList.add(remoteUsername)
    }
    usernamesList.add(username)
    return usernamesList
  }
  
  private fun CoroutineScope.updateUserDataAsync(
    username: String,
    key: String,
    value: Any
  ) = async(dispatchers.IO) {
    reference.child(users)
        .child(username)
        .child(key)
        .setValue(value)
        .await()
  }
}