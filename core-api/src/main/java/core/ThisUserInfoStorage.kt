package core

/**
 * Stores information about current user and provides information about it
 *
 * @see ThisUserInfoProvider
 */
interface ThisUserInfoStorage : ThisUserInfoProvider {
  
  /**
   * Saves [username] of this user
   */
  suspend fun saveUsername(username: String)
  
  /**
   * Saves [email] of this user
   */
  suspend fun saveEmail(email: String)
  
  /**
   * Returns email or null if email isn't saved
   */
  suspend fun getEmailOrNull(): String?
  
  /**
   * Clears all user's data
   */
  suspend fun clear()
}