package firebase.impl

import firebase.database.UsersDatabaseSchema
import firebase.database.UsersDatabaseSchema.Companion.email
import firebase.database.UsersDatabaseSchema.Companion.friend_requests_from_me
import firebase.database.UsersDatabaseSchema.Companion.friend_requests_to_me
import firebase.database.UsersDatabaseSchema.Companion.friends
import firebase.database.UsersDatabaseSchema.Companion.usernames
import firebase.database.UsersDatabaseSchema.Companion.users
import core.model.User

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