package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.email
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.friend_requests_from_me
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.friend_requests_to_me
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.friends
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.usernames
import com.arsvechkarev.firebase.database.UsersDatabaseSchema.Companion.users

object PathDatabaseSchema : UsersDatabaseSchema {
  
  override val allUsernamesPath = withSlashes(usernames)
  
  override fun emailPath(user: User): String {
    return withSlashes(users, user.username, email)
  }
  
  override fun friendsPath(user: User): String {
    return withSlashes(users, user.username, friends)
  }
  
  override fun friendsRequestsToUserPath(user: User): String {
    return withSlashes(users, user.username, friend_requests_to_me)
  }
  
  override fun friendsRequestsFromUserPath(user: User): String {
    return withSlashes(users, user.username, friend_requests_from_me)
  }
  
  private fun withSlashes(vararg args: String) = buildString {
    args.forEach { arg -> append(arg).append("/") }
  }
}