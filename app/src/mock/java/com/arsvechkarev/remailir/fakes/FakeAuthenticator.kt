package com.arsvechkarev.remailir.fakes

import core.Authenticator

object FakeAuthenticator : Authenticator {
  
  override fun isUserLoggedIn(): Boolean {
    return true
  }
  
  override fun isSignInWithEmailLink(emailLink: String): Boolean {
    return false
  }
  
  override suspend fun sendSignInLinkToEmail(email: String) {
  }
  
  override suspend fun signInWithEmailLink(email: String, emailLink: String) {
  }
  
  override fun signOut() {
  }
}