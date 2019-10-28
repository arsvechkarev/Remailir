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
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.setTitle
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.signin.R
import com.arsvechkarev.signin.di.DaggerSignInComponent
import kotlinx.android.synthetic.main.fragment_sign_in.buttonLogIn
import kotlinx.android.synthetic.main.fragment_sign_in.buttonRegister
import kotlinx.android.synthetic.main.fragment_sign_in.editTextEmail
import kotlinx.android.synthetic.main.fragment_sign_in.editTextPassword
import kotlinx.android.synthetic.main.fragment_sign_in.textFailure
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
    setTitle(R.string.title_sign_in)
    injectThis()
    signInViewModel = viewModel(viewModelFactory) {
      observe(state, ::updateState)
    }
    
    buttonLogIn.setOnClickListener {
      signInViewModel.onSignInClick(
        editTextEmail.string(),
        editTextPassword.string()
      )
    }
    
    buttonRegister.setOnClickListener {
      (activity as EntranceActivity).goToSighUp()
    }
  }
  
  private fun injectThis() {
    DaggerSignInComponent.create().inject(this)
  }
  
  private fun updateState(state: SignInState) {
    when (state) {
      is Failure -> {
        textFailure.text = state.throwable.message
      }
      is PreCheckFailure -> textFailure.text = "lol"
      is Success -> {
        (activity as EntranceActivity).goToBase()
      }
    }
  }
}
