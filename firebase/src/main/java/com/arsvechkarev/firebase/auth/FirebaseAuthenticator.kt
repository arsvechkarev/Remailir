package com.arsvechkarev.firebase.auth

import com.arsvechkarev.core.extenstions.await
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

object FirebaseAuthenticator : Authenticator {
  
  private val auth = FirebaseAuth.getInstance()
  
  override fun isUserLoggedIn(): Boolean {
    return userHasDisplayName()
  }
  
  override fun userHasDisplayName(): Boolean {
    val user = auth.currentUser
    return user != null && !user.displayName.isNullOrBlank()
  }
  
  override fun isSignInWithEmailLink(email: String): Boolean {
    return auth.isSignInWithEmailLink(email)
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
  
  override suspend fun sendSignInLinkToEmail(
    email: String,
    settings: ActionCodeSettings
  ) {
    auth.sendSignInLinkToEmail(email, settings).await()
  }
  
  override suspend fun signInWithEmailLink(
    email: String,
    emailLink: String
  ) = auth.signInWithEmailLink(email, emailLink).await()!!
}