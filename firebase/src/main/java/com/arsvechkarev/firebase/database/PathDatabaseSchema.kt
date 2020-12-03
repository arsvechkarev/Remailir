package com.arsvechkarev.firebase.database

import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.email
import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.friend_requests_from_me
import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.friend_requests_to_me
import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.friends
import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.usernames
import com.arsvechkarev.firebase.database.DatabaseSchema.Companion.users
import com.arsvechkarev.firebase.path

object PathDatabaseSchema : DatabaseSchema {
  
  override val allUsersPath = path(usernames)
  
  override fun emailPath(username: String): String {
    return path(users, username, email)
  }
  
  override fun friendsPath(username: String): String {
    return path(users, username, friends)
  }
  
  override fun friendsRequestsToMePath(username: String): String {
    return path(users, username, friend_requests_to_me)
  }
  
  override fun friendsRequestsFromMePath(username: String): String {
    return path(users, username, friend_requests_from_me)
  }
}