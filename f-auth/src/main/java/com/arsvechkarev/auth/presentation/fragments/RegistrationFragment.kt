package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.viewmodels.RegistrationViewModel
import com.arsvechkarev.auth.presentation.viewmodels.UserCreationState
import com.arsvechkarev.auth.presentation.viewmodels.UserCreationState.Completed
import com.arsvechkarev.auth.presentation.viewmodels.UserCreationState.Failed
import com.arsvechkarev.auth.presentation.viewmodels.UserCreationState.NameOccupied
import core.base.BaseFragment
import core.declaration.entranceActivity
import core.extensions.observe
import core.extensions.showToast
import core.extensions.string
import core.extensions.viewModelOf
import firebase.DEFAULT_IMG_URL
import storage.Database
import kotlinx.android.synthetic.main.fragment_registration.buttonSignUp
import kotlinx.android.synthetic.main.fragment_registration.editTextUsername
import kotlinx.android.synthetic.main.fragment_registration.inputLayoutUsername
import javax.inject.Inject

class RegistrationFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy { viewModelOf<RegistrationViewModel>(viewModelFactory) }
  
  override val layout: Int = R.layout.fragment_registration
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.creationState().observe(this, ::handleState)
    handleEditTexts()
    buttonSignUp.setOnClickListener {
      viewModel.createUser(editTextUsername.string())
    }
  }
  
  private fun handleState(state: UserCreationState) {
    when (state) {
      is Completed -> {
        Database.saveUser(context!!, editTextUsername.string(), DEFAULT_IMG_URL)
        entranceActivity.goToBase()
      }
      is NameOccupied -> inputLayoutUsername.error = "User with this name already exists"
      is Failed -> showToast("Something went wrong")
    }
  }
  
  private fun handleEditTexts() {
    editTextUsername.doAfterTextChanged {
      inputLayoutUsername.error = null
      buttonSignUp.isEnabled = it?.toString()?.isNotBlank() == true
    }
  }
}
