package com.arsvechkarev.firebase.auth

import com.arsvechkarev.core.extenstions.await
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

object FirebaseAuthenticator : Authenticator {
  
  override fun isUserLoggedIn(): Boolean {
    return userHasDisplayName()
  }
  
  override fun userHasDisplayName(): Boolean {
    val user = FirebaseAuth.getInstance().currentUser
    return user != null && !user.displayName.isNullOrBlank()
  }
  
  override fun isSignInWithEmailLink(email: String): Boolean {
    return FirebaseAuth.getInstance().isSignInWithEmailLink(email)
  }
  
  override suspend fun saveUsername(username: String) {
    val profileUpdates = UserProfileChangeRequest.Builder()
        .setDisplayName(username)
        .build()
    FirebaseAuth.getInstance().currentUser!!
        .updateProfile(profileUpdates)
        .await()
  }
  
  override suspend fun sendSignInLinkToEmail(
    email: String,
    settings: ActionCodeSettings
  ) {
    FirebaseAuth.getInstance()
        .sendSignInLinkToEmail(email, settings)
        .await()
  }
  
  override suspend fun signInWithEmailLink(
    email: String,
    emailLink: String
  ): AuthResult {
    return FirebaseAuth.getInstance()
        .signInWithEmailLink(email, emailLink)
        .await()!!
  }
}