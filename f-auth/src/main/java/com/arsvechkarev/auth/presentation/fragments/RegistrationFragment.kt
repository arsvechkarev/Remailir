package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.viewmodels.RegistrationViewModel
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.extensions.viewModelOf
import kotlinx.android.synthetic.main.fragment_registration.buttonSignIn
import kotlinx.android.synthetic.main.fragment_registration.editTextPassword
import kotlinx.android.synthetic.main.fragment_registration.editTextRepeatPassword
import kotlinx.android.synthetic.main.fragment_registration.editTextUsername
import javax.inject.Inject

class RegistrationFragment : BaseFragment() {
  
  private var usernameIsNotEmpty = false
  private var passwordIsNotEmpty = false
  private var passwordRepeatIsNotEmpty = false
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy { viewModelOf<RegistrationViewModel>(viewModelFactory) }
  
  override val layout: Int = R.layout.fragment_registration
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    editTextUsername.doAfterTextChanged {
      usernameIsNotEmpty = it?.toString()?.isNotBlank() == true
      updateVisibility()
    }
    editTextPassword.doAfterTextChanged {
      passwordIsNotEmpty = it?.toString()?.isNotBlank() == true
      updateVisibility()
    }
    editTextRepeatPassword.doAfterTextChanged {
      passwordRepeatIsNotEmpty = it?.toString()?.isNotBlank() == true
      updateVisibility()
    }
  }
  
  private fun updateVisibility() {
    buttonSignIn.isEnabled = usernameIsNotEmpty && passwordIsNotEmpty && passwordRepeatIsNotEmpty
  }
}
