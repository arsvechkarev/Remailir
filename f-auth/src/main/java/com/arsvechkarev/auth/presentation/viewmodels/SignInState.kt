package com.arsvechkarev.auth.presentation.viewmodels

sealed class SignInState {
  object Completed : SignInState()
  object IncorrectPassword : SignInState()
  class Failed(val exception: Exception) : SignInState()
}