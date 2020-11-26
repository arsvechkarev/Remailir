package com.arsvechkarev.firebase.database

/**
 * Database for performing operations with user data
 */
interface UserInfoDatabase {
  
  /**
   * Saves user to the database
   */
  suspend fun saveUserInfo(username: String, email: String)
  
  /**
   * Removes friend from [thisUsername] from [otherUsername]
   */
  suspend fun removeFriend(thisUsername: String, otherUsername: String)
  
  suspend fun cancelMyRequest(thisUsername: String, otherUsername: String)
  
  suspend fun acceptRequest(thisUsername: String, otherUsername: String)
  
  suspend fun dismissRequest(thisUsername: String, otherUsername: String)
  
  /**
   * Returns list located in the given [path] with elements mapped with [mapper]
   */
  suspend fun <T> getList(path: String, mapper: (String) -> T): MutableList<T>
  
  /**
   * Add [value] located in the given [path]
   */
  suspend fun addValue(path: String, value: String)
  
  /**
   * Removes [value] located in the given [path]. Returns true, if was successfully
   * deleted and false, if value was not found
   */
  suspend fun removeValue(path: String, value: String): Boolean
}