package com.arsvechkarev.auth

class SignInCorrectnessState(
  val emailState: InputState,
  val passwordState: InputState
)


fun SignInCorrectnessState.notAllCorrect(): Boolean {
  return !(emailState == InputState.CORRECT
      && passwordState == InputState.CORRECT)
}

class SignUpCorrectnessState(
  val userNameState: InputState,
  val emailState: InputState,
  val passwordState: InputState
)

fun SignUpCorrectnessState.notAllCorrect(): Boolean {
  return !(userNameState == InputState.CORRECT
      && emailState == InputState.CORRECT
      && passwordState == InputState.CORRECT)
}

enum class InputState {
  CORRECT,
  EMPTY,
  INCORRECT,
  TOO_SHORT
}