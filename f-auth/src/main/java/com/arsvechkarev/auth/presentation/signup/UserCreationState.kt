package com.arsvechkarev.auth.presentation.signup

sealed class UserCreationState {
  
  object Completed : UserCreationState()
  object NameOccupied : UserCreationState()
  class Failed(val exception: Exception) : UserCreationState()
  
}