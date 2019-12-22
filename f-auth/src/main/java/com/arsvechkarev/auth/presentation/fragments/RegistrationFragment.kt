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
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModelOf
import com.arsvechkarev.firebase.DEFAULT_IMG_URL
import com.arsvechkarev.storage.Database
import kotlinx.android.synthetic.main.fragment_registration.buttonSignUp
import kotlinx.android.synthetic.main.fragment_registration.editTextUsername
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
      is Failed -> showToast("Something went wrong")
    }
  }
  
  private fun handleEditTexts() {
    editTextUsername.doAfterTextChanged {
      buttonSignUp.isEnabled = it?.toString()?.isNotBlank() == true
    }
  }
}
