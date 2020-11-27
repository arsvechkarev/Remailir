package com.arsvechkarev.firebase.database

import com.arsvechkarev.firebase.database.Schema.Companion.friend_requests_from_me
import com.arsvechkarev.firebase.database.Schema.Companion.friend_requests_to_me
import com.arsvechkarev.firebase.database.Schema.Companion.friends
import com.arsvechkarev.firebase.database.Schema.Companion.usernames
import com.arsvechkarev.firebase.database.Schema.Companion.users

object FirebaseSchema : Schema {
  
  override val allUsersPath = path(usernames)
  
  override fun friendsPath(username: String): String {
    return path(users, username, friends)
  }
  
  override fun friendsRequestsToMePath(username: String): String {
    return path(users, username, friend_requests_to_me)
  }
  
  override fun friendsRequestsFromMePath(username: String): String {
    return path(users, username, friend_requests_from_me)
  }
  
  private fun path(vararg args: String) = buildString {
    for (arg in args) {
      append(arg)
      append("/")
    }
  }
}