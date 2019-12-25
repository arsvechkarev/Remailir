package com.arsvechkarev.remailir.entrance

import core.model.users.NewUser

sealed class PhoneAuthState {
  
  class UserAlreadyExists(val user: NewUser) : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Exception) : PhoneAuthState()
  
}