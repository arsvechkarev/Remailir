package com.arsvechkarev.signin.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.SignInState
import com.arsvechkarev.auth.SignInState.Failure
import com.arsvechkarev.auth.SignInState.PreCheckFailure
import com.arsvechkarev.auth.SignInState.Success
import com.arsvechkarev.core.declaration.EntranceActivity
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.extensions.afterTextChanged
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModelOf
import com.arsvechkarev.signin.R
import com.arsvechkarev.signin.di.DaggerSignInComponent
import kotlinx.android.synthetic.main.fragment_sign_in.buttonLogIn
import kotlinx.android.synthetic.main.fragment_sign_in.editTextEmail
import kotlinx.android.synthetic.main.fragment_sign_in.editTextPassword
import kotlinx.android.synthetic.main.fragment_sign_in.textFailure
import kotlinx.android.synthetic.main.fragment_sign_in.textSignIn
import javax.inject.Inject


class SignInFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var signInViewModel: SignInViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_sign_in, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerSignInComponent.create().inject(this)
    signInViewModel = viewModelOf(viewModelFactory) {
      observe(state, ::updateState)
    }
    
    buttonLogIn.setOnClickListener {
      signInViewModel.onSignInClick(
        editTextEmail.string(),
        editTextPassword.string()
      )
    }
  
    textSignIn.setOnClickListener {
      (activity as EntranceActivity).goToSighUp()
    }
  
    editTextEmail.afterTextChanged { defineSignUpButtonState() }
    editTextPassword.afterTextChanged { defineSignUpButtonState() }
  }
  
  
  private fun defineSignUpButtonState() {
    buttonLogIn.isEnabled = (
        editTextEmail.string().isNotBlank()
            && editTextPassword.string().isNotBlank()
        )
  }
  
  private fun updateState(state: SignInState) {
    when (state) {
      is Failure -> {
        textFailure.text = state.throwable.message
      }
      is PreCheckFailure -> textFailure.text = "lol"
      is Success -> {
        entranceActivity.goToBase()
      }
    }
  }
}
