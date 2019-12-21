package com.arsvechkarev.auth.presentation.viewmodels

sealed class UserCreationState {
  
  object Completed : UserCreationState()
  class Failed(exception: Exception) : UserCreationState()
  
}