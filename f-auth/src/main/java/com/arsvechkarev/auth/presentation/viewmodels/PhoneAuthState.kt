package com.arsvechkarev.auth.presentation.viewmodels

sealed class PhoneAuthState {
  
  object UserAlreadyExists : PhoneAuthState()
  object UserNotExist : PhoneAuthState()
  class Failed(exception: Exception) : PhoneAuthState()
  object Cancelled : PhoneAuthState()
  
}