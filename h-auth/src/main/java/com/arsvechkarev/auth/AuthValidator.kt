package com.arsvechkarev.auth

import javax.inject.Inject

class AuthValidator @Inject constructor() {
  
  fun checkUsername(username: String): CheckingState {
    if (username.length < MIN_USERNAME_LENGTH) {
      return CheckingState.TOO_SHORT
    }
    if (username.isBlank()) {
      return CheckingState.INCORRECT
    }
    return CheckingState.CORRECT
  }
  
  fun checkEmail(email: String): CheckingState {
    if (email.isBlank()) {
      return CheckingState.INCORRECT
    }
    return CheckingState.CORRECT
  }
  
  fun checkPassword(password: String): CheckingState {
    if (password.length < MIN_PASSWORD_LENGTH) {
      return CheckingState.TOO_SHORT
    }
    return CheckingState.CORRECT
  }
  
  
  fun preCheck(username: String, email: String, password: String): SignUpCorrectnessState {
    return SignUpCorrectnessState(
      userNameState = checkInput(username),
      emailState = checkInput(email),
      passwordState = checkPassword2(password)
    )
  }
  
  fun preCheck(email: String, password: String): SignInCorrectnessState {
    return SignInCorrectnessState(
      emailState = checkInput(email),
      passwordState = checkPassword2(password)
    )
  }
  
  private fun checkInput(input: String): InputState {
    return when {
      input.isEmpty() -> InputState.EMPTY
      input.isBlank() -> InputState.INCORRECT
      else -> InputState.CORRECT
    }
  }
  
  private fun checkPassword2(password: String): InputState {
    return when {
      password.isEmpty() -> InputState.EMPTY
      password.isBlank() -> InputState.INCORRECT
      password.length < MIN_PASSWORD_LENGTH -> InputState.TOO_SHORT
      else -> InputState.CORRECT
    }
  }
}