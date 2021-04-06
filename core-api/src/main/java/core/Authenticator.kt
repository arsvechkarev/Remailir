package core

interface Authenticator {
  
  /**
   * Tells whether user is logged in or not
   */
  fun isUserLoggedIn(): Boolean
  
  /**
   * Tells whether [emailLink] is used for signing in
   */
  fun isSignInWithEmailLink(emailLink: String): Boolean
  
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