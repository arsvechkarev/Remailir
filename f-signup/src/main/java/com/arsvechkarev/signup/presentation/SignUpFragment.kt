package com.arsvechkarev.signup.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.CheckingState
import com.arsvechkarev.auth.CheckingState.CORRECT
import com.arsvechkarev.auth.CheckingState.INCORRECT
import com.arsvechkarev.auth.CheckingState.TOO_SHORT
import com.arsvechkarev.auth.SignUpState
import com.arsvechkarev.auth.SignUpState.Failure
import com.arsvechkarev.auth.SignUpState.Success
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.onClick
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModelOf
import com.arsvechkarev.signup.R
import com.arsvechkarev.signup.di.DaggerSignUpComponent
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.fragment_sign_up.buttonRegister
import kotlinx.android.synthetic.main.fragment_sign_up.editTextEmail
import kotlinx.android.synthetic.main.fragment_sign_up.editTextPassword
import kotlinx.android.synthetic.main.fragment_sign_up.editTextUsername
import kotlinx.android.synthetic.main.fragment_sign_up.inputLayoutEmail
import kotlinx.android.synthetic.main.fragment_sign_up.inputLayoutPassword
import kotlinx.android.synthetic.main.fragment_sign_up.inputLayoutUsername
import kotlinx.android.synthetic.main.fragment_sign_up.textLogIn
import javax.inject.Inject

class SignUpFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var signUpViewModel: SignUpViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_sign_up, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    injectThis()
    signUpViewModel = viewModelOf(viewModelFactory) {
      observe(generalState, ::updateGeneralState)
      observe(usernameState, ::handleUsernameState)
      observe(emailState, ::handleEmailState)
      observe(passwordState, ::handlePasswordState)
    }
    
    buttonRegister.setOnClickListener {
      signUpViewModel.onRegisterClick(
        editTextUsername.string(),
        editTextEmail.string(),
        editTextPassword.string()
      )
    }
  
    textLogIn.onClick(entranceActivity::goToSignIn)
  
    editTextUsername.handleTyping(inputLayoutUsername, this::defineSignUpButtonState)
    editTextEmail.handleTyping(inputLayoutEmail, this::defineSignUpButtonState)
    editTextPassword.handleTyping(inputLayoutPassword, this::defineSignUpButtonState)
  }
  
  private fun handleUsernameState(state: CheckingState) {
    when (state) {
      INCORRECT -> inputLayoutUsername.error = "Invalid format"
      TOO_SHORT -> inputLayoutUsername.error = "Username is too short"
      CORRECT -> inputLayoutUsername.error = null
    }
  }
  
  private fun handleEmailState(state: CheckingState) {
    when (state) {
      INCORRECT -> inputLayoutEmail.error = "Invalid format"
      TOO_SHORT -> inputLayoutEmail.error = "Email is too short"
      CORRECT -> inputLayoutEmail.error = null
    }
  }
  
  private fun handlePasswordState(state: CheckingState) {
    when (state) {
      INCORRECT -> inputLayoutPassword.error = "Invalid format"
      TOO_SHORT -> inputLayoutPassword.error = "Password should be at least 6 symbols"
      CORRECT -> inputLayoutPassword.error = null
    }
  }
  
  private fun defineSignUpButtonState() {
    buttonRegister.isEnabled = (
        editTextUsername.string().isNotBlank()
            && editTextEmail.string().isNotBlank()
            && editTextPassword.string().isNotBlank()
        )
  }
  
  private fun injectThis() {
    DaggerSignUpComponent.create().inject(this)
  }
  
  private fun updateGeneralState(state: SignUpState) {
    when (state) {
      is Failure -> {
        Log.d("qwerty", state.error.message, state.error)
        when (state.error) {
          is FirebaseAuthInvalidCredentialsException -> {
            inputLayoutEmail.error = state.error.message
          }
        }
        showToast("Error: ${state.error.message}")
      }
      is Success -> {
        entranceActivity.goToBase()
      }
    }
  }
}
