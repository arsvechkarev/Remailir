package core.impl

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import core.Authenticator
import core.utils.await

object FirebaseAuthenticator : Authenticator {
  
  private const val SIGN_IN_LINK = "https://remailir.com/auth"
  private const val PACKAGE_NAME = "com.arsvechkarev.remailir"
  private const val VERSION = "1"
  
  private val firebaseAuth = FirebaseAuth.getInstance()
  
  override fun isUserLoggedIn(): Boolean {
    return firebaseAuth.currentUser != null
  }
  
  override fun isSignInWithEmailLink(emailLink: String): Boolean {
    return firebaseAuth.isSignInWithEmailLink(emailLink)
  }
  
  override suspend fun sendSignInLinkToEmail(email: String) {
    val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl(SIGN_IN_LINK)
        .setHandleCodeInApp(true)
        .setAndroidPackageName(PACKAGE_NAME, true, VERSION)
        .build()
    firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings).await()
  }
  
  override suspend fun signInWithEmailLink(email: String, emailLink: String) {
    firebaseAuth.signInWithEmailLink(email, emailLink).await()!!
  }
  
  override fun signOut() {
    firebaseAuth.signOut()
  }
}