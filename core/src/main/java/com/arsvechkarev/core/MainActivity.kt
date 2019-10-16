package com.arsvechkarev.core

import com.arsvechkarev.core.model.Friend

interface MainActivity {
  
  fun goToChat(friend: Friend)
  
  fun goToBase()
  
  fun goToProfile()
  
  fun goToMessages()
  
  fun goToSignIn()
  
  fun goToSighUp()
  
  fun signOut()
}