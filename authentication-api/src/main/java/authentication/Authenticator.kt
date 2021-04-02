package authentication

import core.model.User

interface Authenticator {
  
  /**
   * Tells whether user is logged in or not
   */
  fun isUserLoggedIn(): Boolean
  
  /**
   * Tells whether user already has display name or not
   */
  fun userHasDisplayName(): Boolean
  
  /**
   * Tells whether [emailLink] is used for signing in
   */
  fun isSignInWithEmailLink(emailLink: String): Boolean
  
  /**
   * Returns username of saved user
   */
  fun getUsername(): String
  
  /**
   * Returns current user
   */
  fun getUser(): User
  
  /**
   * Returns email of saved user
   */
  fun getEmail(): String
  
  /**
   * Saves [username] to backend
   */
  suspend fun saveUsername(username: String)
  
  /**
   * Sends link for signing in to [email]
   */
  suspend fun sendSignInLinkToEmail(email: String)
  
  /**
   * Tries to sign in with [email] and [emailLink]
   */
  suspend fun signInWithEmailLink(email: String, emailLink: String)
  
  /**
   * Signs out
   */
  fun signOut()
}