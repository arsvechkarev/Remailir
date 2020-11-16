package com.arsvechkarev.core.auth

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult

interface Authenticator {
  
  fun isUserLoggedIn(): Boolean
  
  fun isSignInWithEmailLink(email: String): Boolean
  
  suspend fun sendSignInLinkToEmail(email: String, settings: ActionCodeSettings)
  
  suspend fun signInWithEmailLink(email: String, emailLink: String): AuthResult
}