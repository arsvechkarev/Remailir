package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.viewmodels.SignInState
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.Completed
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.Failed
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.IncorrectPassword
import com.arsvechkarev.auth.presentation.viewmodels.SignInViewModel
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModelOf
import kotlinx.android.synthetic.main.fragment_sign_in.buttonCheckPassword
import kotlinx.android.synthetic.main.fragment_sign_in.editTextPassword
import javax.inject.Inject

class SignInFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy { viewModelOf<SignInViewModel>(viewModelFactory) }
  
  override val layout: Int = R.layout.fragment_sign_in
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.signInState().observe(this) {
      when (it) {
        is Completed -> entranceActivity.goToBase()
        is IncorrectPassword -> showToast("Password is incorrect")
        is Failed -> showToast("Failure")
      }
    }
    buttonCheckPassword.setOnClickListener {
      viewModel.signIn(editTextPassword.string())
    }
  }
}
