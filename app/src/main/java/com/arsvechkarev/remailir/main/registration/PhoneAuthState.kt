package com.arsvechkarev.remailir.main.registration

sealed class PhoneAuthState {
  
  object UserAlreadyExists : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  class OnCodeSent(val verificationId: String) : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  class Failed(val exception: Exception) : PhoneAuthState()
  
}