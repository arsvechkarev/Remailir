package com.arsvechkarev.remailir.entrance

sealed class PhoneAuthState {
  
  object UserAlreadyExists : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Exception) : PhoneAuthState()
  
}