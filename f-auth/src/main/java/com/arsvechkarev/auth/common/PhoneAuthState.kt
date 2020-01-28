package com.arsvechkarev.auth.common

import core.model.users.User

sealed class PhoneAuthState {
  
  /**
   * When user account is found and we can safely proceed
   */
  class UserAlreadyExists(val user: User) : PhoneAuthState()
  
  /**
   * User is not exist, redirect to registration
   */
  object UserNotExist : PhoneAuthState()
  
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Throwable?) : PhoneAuthState()
  
}