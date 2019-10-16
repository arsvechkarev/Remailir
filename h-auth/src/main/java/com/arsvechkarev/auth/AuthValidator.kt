package com.arsvechkarev.auth

import javax.inject.Inject

class AuthValidator @Inject constructor() {
  
  fun preCheck(username: String, email: String, password: String): SignUpCorrectnessState {
    return SignUpCorrectnessState(
      userNameState = checkInput(username),
      emailState = checkInput(email),
      passwordState = checkPassword(password)
    )
  }
  
  fun preCheck(email: String, password: String): SignInCorrectnessState {
    return SignInCorrectnessState(
      emailState = checkInput(email),
      passwordState = checkPassword(password)
    )
  }
  
  private fun checkInput(input: String): InputState {
    return when {
      input.isEmpty() -> InputState.EMPTY
      input.isBlank() -> InputState.INCORRECT
      else -> InputState.CORRECT
    }
  }
  
  private fun checkPassword(password: String): InputState {
    return when {
      password.isEmpty() -> InputState.EMPTY
      password.isBlank() -> InputState.INCORRECT
      password.length < MIN_PASSWORD_LENGTH -> InputState.TOO_SHORT
      else -> InputState.CORRECT
    }
  }
}