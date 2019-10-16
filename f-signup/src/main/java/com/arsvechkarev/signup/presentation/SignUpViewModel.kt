package com.arsvechkarev.signup.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.SignUpState
import com.arsvechkarev.auth.AuthValidator
import com.arsvechkarev.auth.notAllCorrect
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.signup.repositories.SignUpRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
  private val validator: AuthValidator,
  private val repository: SignUpRepository
) : BaseViewModel() {
  
  val state = MutableLiveData<SignUpState>()
  
  fun onRegisterClick(username: String, email: String, password: String) {
    val correctnessState = validator.preCheck(username, email, password)
    if (correctnessState.notAllCorrect()) {
      state.value = SignUpState.PreCheckFailure(correctnessState)
      return
    }
    repository.setParams(username, email, password)
      .onSuccess { state.value = SignUpState.Success }
      .onFailure { state.value = defineFailure(it) }
      .execute()
  }
  
  private fun defineFailure(it: Throwable): SignUpState.Failure {
    return SignUpState.Failure(it)
  }
  
}