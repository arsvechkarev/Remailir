package com.arsvechkarev.remailir.entrance

import core.model.users.User

sealed class PhoneAuthState {
  
  class UserAlreadyExists(val user: User) : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Exception?) : PhoneAuthState()
  
}