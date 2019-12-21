package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.viewmodels.PhoneAuthState
import com.arsvechkarev.auth.presentation.viewmodels.PhoneAuthState.Cancelled
import com.arsvechkarev.auth.presentation.viewmodels.PhoneAuthState.Failed
import com.arsvechkarev.auth.presentation.viewmodels.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.auth.presentation.viewmodels.PhoneAuthState.UserNotExist
import com.arsvechkarev.auth.presentation.viewmodels.PhoneViewModel
import com.arsvechkarev.auth.utils.phoneNumber
import com.arsvechkarev.auth.utils.removeDashes
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.viewModelOf
import com.arsvechkarev.core.model.Country
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode
import javax.inject.Inject

class PhoneFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy { viewModelOf<PhoneViewModel>(viewModelFactory) }
  
  override val layout: Int = R.layout.fragment_phone
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.phoneState().observe(this, ::handleState)
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
      override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        buttonNext.isEnabled = s.removeDashes().length >= 10
      }
    })
    buttonNext.setOnClickListener {
      viewModel.sendNumber(textCountryCode.text.toString(), editTextPhone.phoneNumber(), activity!!)
    }
  }
  
  private fun handleState(authState: PhoneAuthState) {
    when (authState) {
      is UserAlreadyExists -> entranceActivity.goToBase()
      is UserNotExist -> entranceActivity.goToFragment(RegistrationFragment())
      is Failed -> showToast("Failed")
      is Cancelled -> showToast("Cancelled")
    }
  }
  
  fun onCountrySelected(country: Country) {
    textCountryCode.text = "+${country.code}"
  }
  
}
