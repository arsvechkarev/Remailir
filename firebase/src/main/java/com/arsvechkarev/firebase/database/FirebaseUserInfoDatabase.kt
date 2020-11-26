package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.exceptions.UsernameAlreadyExistsException
import com.arsvechkarev.core.extenstions.assertThat
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.extenstions.waitForSingleValueEvent
import com.arsvechkarev.firebase.database.Schema.Companion.friend_requests_from_me
import com.arsvechkarev.firebase.database.Schema.Companion.friend_requests_to_me
import com.arsvechkarev.firebase.database.Schema.Companion.friends
import com.arsvechkarev.firebase.database.Schema.Companion.usernames
import com.arsvechkarev.firebase.database.Schema.Companion.users
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class FirebaseUserInfoDatabase(
  private val schema: Schema,
  private val dispatchers: Dispatchers,
) : UserInfoDatabase {
  
  private val reference = FirebaseDatabase.getInstance().reference
  
  override suspend fun saveUserInfo(
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
  
  override suspend fun removeFriend(
    thisUsername: String, otherUsername: String
  ) = withContext(dispatchers.IO) {
    val path = ""
    val value = ""
    val snapshot = reference.child(path).waitForSingleValueEvent()
    if (!snapshot.exists() || !snapshot.hasChildren()) {
      return@withContext // Most likely, value has already been deleted by other user
    }
    val list = ArrayList<String>()
    var totalChildren = 0
    for (snap in snapshot.children) {
      val snapshotValue = snap.value as String
      if (snapshotValue != value) {
        list.add(snapshotValue)
      }
      totalChildren++
    }
    if (list.size == totalChildren) {
      // Value hasn't been found, nothing to delete
      return@withContext
    } else {
      // Updating list
      if (list.isEmpty()) {
        reference.child(path).setValue("").await()
      } else {
        reference.child(path).setValue(list).await()
      }
      return@withContext
    }
  }
  
  override suspend fun cancelMyRequest(thisUsername: String, otherUsername: String) {
  }
  
  override suspend fun acceptRequest(thisUsername: String, otherUsername: String) {
  }
  
  override suspend fun dismissRequest(thisUsername: String, otherUsername: String) {
  }
  
  override suspend fun <T> getList(
    path: String, mapper: (String) -> T
  ): MutableList<T> = withContext(dispatchers.IO) {
    val snapshot = reference.child(path).waitForSingleValueEvent()
    if (!snapshot.exists() || !snapshot.hasChildren()) {
      return@withContext ArrayList()
    }
    val list = ArrayList<T>()
    for (snap in snapshot.children) {
      val value = snap.value as String
      assertThat(value.isNotBlank())
      list.add(mapper(value))
    }
    return@withContext list
  }
  
  override suspend fun addValue(
    path: String, value: String
  ): Unit = withContext(dispatchers.IO) {
    val snapshot = reference.child(path).waitForSingleValueEvent()
    assertThat(snapshot.exists())
    val list = ArrayList<String>()
    list.add(value)
    for (snap in snapshot.children) {
      list.add(snap.value as String)
    }
    reference.child(path).setValue(list)
  }
  
  override suspend fun removeValue(
    path: String, value: String
  ) = withContext(dispatchers.IO) {
    val snapshot = reference.child(path).waitForSingleValueEvent()
    if (!snapshot.exists() || !snapshot.hasChildren()) {
      return@withContext false // Most likely, value has already been deleted by other user
    }
    val list = ArrayList<String>()
    var totalChildren = 0
    for (snap in snapshot.children) {
      val snapshotValue = snap.value as String
      if (snapshotValue != value) {
        list.add(snapshotValue)
      }
      totalChildren++
    }
    if (list.size == totalChildren) {
      // Value hasn't been found, nothing to delete
      return@withContext false
    } else {
      // Updating list
      if (list.isEmpty()) {
        reference.child(path).setValue("").await()
      } else {
        reference.child(path).setValue(list).await()
      }
      return@withContext true
    }
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