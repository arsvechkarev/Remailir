package com.arsvechkarev.firebase.database

interface Schema {
  
  val allUsersPath: String
  
  fun friendsPath(username: String): String
  
  fun friendsRequestsToMePath(username: String): String
  
  fun friendsRequestsFromMePath(username: String): String
  
  companion object {
    
    const val usernames = "usernames"
    const val users = "users"
    const val email = "email"
    const val friend_requests_from_me = "friend_requests_from_me"
    const val friend_requests_to_me = "friend_requests_to_me"
    const val friends = "friends"
  }
}