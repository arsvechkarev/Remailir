package com.arsvechkarev.registration.domain

import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_EMPTY
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_INVALID
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_VALID
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_CONTAINS_PROHIBITED_SYMBOLS
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_EMPTY
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_VALID

object Validator {
  
  private val regex = Regex("^[a-zA-Z0-9_]*\$")
  
  fun isEmailIncorrect(email: String): Boolean {
    return validateEmail(email) != EMAIL_VALID
  }
  
  fun isUsernameIncorrect(email: String): Boolean {
    return validateUsername(email) != USERNAME_VALID
  }
  
  fun validateEmail(email: String): EmailState = when {
    email.isBlank() -> EMAIL_EMPTY
    !email.contains("@") -> EMAIL_INVALID
    else -> EMAIL_VALID
  }
  
  fun validateUsername(username: String): UsernameState = when {
    username.isBlank() -> USERNAME_EMPTY
    !username.matches(regex) -> USERNAME_CONTAINS_PROHIBITED_SYMBOLS
    else -> USERNAME_VALID
  }
  
  enum class EmailState {
    EMAIL_VALID, EMAIL_EMPTY, EMAIL_INVALID
  }
  
  enum class UsernameState {
    USERNAME_VALID, USERNAME_EMPTY, USERNAME_CONTAINS_PROHIBITED_SYMBOLS
  }
}
