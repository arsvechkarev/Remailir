package com.arsvechkarev.auth.presentation.registration

sealed class UserCreationState {
  
  object Completed : UserCreationState()
  object NameOccupied : UserCreationState()
  class Failed(val exception: Exception) : UserCreationState()
  
}