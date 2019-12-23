package com.arsvechkarev.auth.presentation.viewmodels

sealed class UserCreationState {
  
  object Completed : UserCreationState()
  object NameOccupied : UserCreationState()
  class Failed(val exception: Exception) : UserCreationState()
  
}