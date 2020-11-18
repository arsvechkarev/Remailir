package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.extenstions.waitForSingleValueEvent
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

class FirebaseDatabase(
  private val dispatchers: Dispatchers,
) : Database {
  
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