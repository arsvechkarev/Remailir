package authentication.impl

import authentication.Authenticator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import core.model.User
import core.utils.await

object FirebaseAuthenticator : Authenticator {
  
  private val auth = FirebaseAuth.getInstance()
  
  override fun isUserLoggedIn(): Boolean {
    return userHasDisplayName()
  }
  
  override fun userHasDisplayName(): Boolean {
    val user = auth.currentUser
    return user != null && !user.displayName.isNullOrBlank()
  }
  
  override fun isSignInWithEmailLink(emailLink: String): Boolean {
    return auth.isSignInWithEmailLink(emailLink)
  }
  
  override fun getUsername(): String {
    return auth.currentUser!!.displayName!!
  }
  
  override fun getUser(): User {
    return User(getUsername())
  }
  
  override fun getEmail(): String {
    return auth.currentUser!!.email!!
  }
  
  override fun signOut() {
    auth.signOut()
  }
  
  override suspend fun saveUsername(username: String) {
    val profileUpdates = UserProfileChangeRequest.Builder()
        .setDisplayName(username)
        .build()
    auth.currentUser!!
        .updateProfile(profileUpdates)
        .await()
  }
  
  override suspend fun sendSignInLinkToEmail(email: String) {
    auth.sendSignInLinkToEmail(email, AuthSettings).await()
  }
  
  override suspend fun signInWithEmailLink(email: String, emailLink: String) {
    auth.signInWithEmailLink(email, emailLink).await()!!
  }
}