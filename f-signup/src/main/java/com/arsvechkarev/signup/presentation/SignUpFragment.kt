package com.arsvechkarev.signup.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.SignUpState
import com.arsvechkarev.auth.SignUpState.Failure
import com.arsvechkarev.auth.SignUpState.PreCheckFailure
import com.arsvechkarev.auth.SignUpState.Success
import com.arsvechkarev.core.base.EntranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.setTitle
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.signup.R
import com.arsvechkarev.signup.di.DaggerSignUpComponent
import kotlinx.android.synthetic.main.fragment_sign_up.buttonLogIn
import kotlinx.android.synthetic.main.fragment_sign_up.buttonRegister
import kotlinx.android.synthetic.main.fragment_sign_up.editTextEmail
import kotlinx.android.synthetic.main.fragment_sign_up.editTextPassword
import kotlinx.android.synthetic.main.fragment_sign_up.editTextUserName
import kotlinx.android.synthetic.main.fragment_sign_up.textFailure
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
    setTitle(R.string.title_sign_up)
    injectThis()
    signUpViewModel = viewModel(viewModelFactory) {
      observe(state, ::updateState)
    }
    
    buttonRegister.setOnClickListener {
      signUpViewModel.onRegisterClick(
        editTextUserName.string(),
        editTextEmail.string(),
        editTextPassword.string()
      )
    }
    
    buttonLogIn.setOnClickListener {
      (activity as EntranceActivity).goToSignIn()
    }
  }
  
  private fun injectThis() {
    DaggerSignUpComponent.create().inject(this)
  }
  
  private fun updateState(state: SignUpState) {
    when (state) {
      is Failure -> {
        textFailure.text = state.throwable.message
      }
      is PreCheckFailure -> {
        textFailure.text = "lol"
      }
      is Success -> {
        (activity as EntranceActivity).goToBase()
      }
    }
  }
}
