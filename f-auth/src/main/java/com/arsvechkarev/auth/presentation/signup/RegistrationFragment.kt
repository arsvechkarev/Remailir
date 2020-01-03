package com.arsvechkarev.auth.presentation.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.signup.UserCreationState.Completed
import com.arsvechkarev.auth.presentation.signup.UserCreationState.Failed
import com.arsvechkarev.auth.presentation.signup.UserCreationState.NameOccupied
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.observe
import core.extensions.showToast
import core.extensions.string
import core.extensions.viewModelOf
import core.strings.DEFAULT_IMG_URL
import kotlinx.android.synthetic.main.fragment_registration.buttonSignUp
import kotlinx.android.synthetic.main.fragment_registration.editTextUsername
import storage.Database
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
      is NameOccupied -> showToast("User with this name already exists")
      is Failed -> showToast("Something went wrong")
    }
  }
  
  private fun handleEditTexts() {
    editTextUsername.doAfterTextChanged {
      buttonSignUp.isEnabled = it?.toString()?.isNotBlank() == true
    }
  }
}
