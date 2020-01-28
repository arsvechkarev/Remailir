package com.arsvechkarev.auth.common

sealed class PhoneAuthState {
  
  /**
   * Used to clear [Failed] state so that fragments don't show error state anymore
   */
  object Pending : PhoneAuthState()
  
  /**
   * When user account is found and we can safely proceed
   */
  object UserAlreadyExists : PhoneAuthState()
  
  /**
   * User is not exist, redirect to registration
   */
  object UserNotExist : PhoneAuthState()
  
  object Cancelled : PhoneAuthState()
  object OnCheckedAutomatically : PhoneAuthState()
  object OnCodeSent : PhoneAuthState()
  class Failed(val exception: Throwable?) : PhoneAuthState()
  
}