package com.arsvechkarev.core.auth

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.arsvechkarev.core.extenstions.await

object FirebaseAuthenticator : Authenticator {
  
  override fun isUserLoggedIn(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
  }
  
  override fun isSignInWithEmailLink(email: String): Boolean {
    return FirebaseAuth.getInstance().isSignInWithEmailLink(email)
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