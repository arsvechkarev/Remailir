package com.arsvechkarev.remailir.main.registration

sealed class PhoneAuthState {
  
  object UserAlreadyExists : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Exception) : PhoneAuthState()
  
}