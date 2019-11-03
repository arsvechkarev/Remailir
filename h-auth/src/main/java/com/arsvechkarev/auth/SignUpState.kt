package com.arsvechkarev.auth

sealed class SignUpState {
  
  object Success : SignUpState()
  
  class PreCheckFailure(val reason: SignUpCorrectnessState) : SignUpState()
  
  class Failure(val error: Throwable) : SignUpState()
}

sealed class SignInState {
  
  object Success : SignInState()
  
  class PreCheckFailure(val reason: SignInCorrectnessState) : SignInState()
  
  class Failure(val throwable: Throwable) : SignInState()
}

enum class CheckingState {
  CORRECT,
  TOO_SHORT,
  INCORRECT;
  
}