package firebase.database

import core.model.User

/**
 * Schema for creating paths to users database
 */
interface UsersDatabaseSchema {
  
  /**
   * Path to list of all usernames in the database
   */
  val allUsernamesPath: String
  
  /**
   * Path to the email field of [user]
   */
  fun emailPath(user: User): String
  
  /**
   * Path to the all friends of [user]
   */
  fun friendsPath(user: User): String
  
  /**
   * Path to the friend requests **to** [user]
   */
  fun friendsRequestsToUserPath(user: User): String
  
  /**
   * Path to the friend requests **from** [user]
   */
  fun friendsRequestsFromUserPath(user: User): String
  
  companion object {
    
    const val usernames = "usernames"
    const val users = "users"
    const val email = "email"
    const val friend_requests_from_me = "friend_requests_from_me"
    const val friend_requests_to_me = "friend_requests_to_me"
    const val friends = "friends"
  }
}