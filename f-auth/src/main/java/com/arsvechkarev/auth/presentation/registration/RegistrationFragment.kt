package com.arsvechkarev.auth.presentation.registration

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.registration.UserCreationState.Completed
import com.arsvechkarev.auth.presentation.registration.UserCreationState.Failed
import com.arsvechkarev.auth.presentation.registration.UserCreationState.NameOccupied
import core.base.BaseFragment
import core.base.entranceActivity
import core.di.coreComponent
import core.extensions.observe
import core.extensions.showToast
import core.extensions.string
import core.extensions.viewModelOf
import kotlinx.android.synthetic.main.fragment_registration.buttonSignUp
import kotlinx.android.synthetic.main.fragment_registration.editTextUsername
import javax.inject.Inject

class RegistrationFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: RegistrationViewModel
  
  override val layout: Int = R.layout.fragment_registration
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerAuthComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(creationState(), ::handleState)
    }
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    editTextUsername.doAfterTextChanged {
      buttonSignUp.isEnabled = it?.toString()?.isNotBlank() == true
    }
    buttonSignUp.setOnClickListener {
      viewModel.createUser(editTextUsername.string())
    }
  }
  
  private fun handleState(state: UserCreationState) {
    when (state) {
      is Completed -> {
        entranceActivity.goToCore()
      }
      is NameOccupied -> showToast("User with this name already exists")
      is Failed -> showToast("Something went wrong")
    }
  }
  
}
