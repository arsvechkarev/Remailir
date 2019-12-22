package com.arsvechkarev.remailir.main.registration

sealed class PhoneAuthState {
  
  object UserAlreadyExists : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  class OnCodeSent(val verificationId: String) : PhoneAuthState()
  class Failed(val exception: Exception) : PhoneAuthState()
  
}