package com.arsvechkarev.signup.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.AuthValidator
import com.arsvechkarev.auth.CheckingState
import com.arsvechkarev.auth.CheckingState.CORRECT
import com.arsvechkarev.auth.SignUpState
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.signup.repositories.SignUpRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
  private val validator: AuthValidator,
  private val repository: SignUpRepository
) : BaseViewModel() {
  
  val generalState = MutableLiveData<SignUpState>()
  
  val usernameState = MutableLiveData<CheckingState>()
  val emailState = MutableLiveData<CheckingState>()
  val passwordState = MutableLiveData<CheckingState>()
  
  fun onRegisterClick(username: String, email: String, password: String) {
    val checkUsername = validator.checkUsername(username)
    val checkEmail = validator.checkEmail(email)
    val checkPassword = validator.checkPassword(password)
    if (checkUsername != CORRECT
      || checkEmail != CORRECT
      || checkPassword != CORRECT
    ) {
      usernameState.value = checkUsername
      emailState.value = checkEmail
      passwordState.value = checkPassword
      return
    }
    repository.setParams(username, email, password)
      .onSuccess { generalState.value = SignUpState.Success }
      .onFailure { generalState.value = SignUpState.Failure(it) }
      .execute()
  }
  
}