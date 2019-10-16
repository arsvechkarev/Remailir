package com.arsvechkarev.signin.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.AuthValidator
import com.arsvechkarev.auth.SignInState
import com.arsvechkarev.auth.SignInState.*
import com.arsvechkarev.auth.notAllCorrect
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.signin.repositories.SignInRepository
import javax.inject.Inject

class SignInViewModel @Inject constructor(
  private val repository: SignInRepository,
  private val validator: AuthValidator
) : BaseViewModel() {
  
  val state = MutableLiveData<SignInState>()
  
  fun onSignInClick(email: String, password: String) {
    val correctnessState = validator.preCheck(email, password)
    if (correctnessState.notAllCorrect()) {
      state.value = PreCheckFailure(correctnessState)
      return
    }
    repository.setParams(email, password)
      .onSuccess { state.value = Success }
      .onFailure { state.value = defineFailure(it) }
      .execute()
  }
  
  private fun defineFailure(it: Throwable): Failure {
    Log.d("Main", "Fail creation: ${it.message}")
    return Failure(it)
  }
  
}