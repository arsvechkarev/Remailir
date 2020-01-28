package com.arsvechkarev.auth.presentation.sms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.common.EntranceViewModel
import com.arsvechkarev.auth.common.PhoneAuthState
import com.arsvechkarev.auth.common.PhoneAuthState.Failed
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import core.base.entranceActivity
import core.di.coreComponent
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.showKeyboard
import core.extensions.viewModelOf
import core.strings.ERROR_INVALID_VERIFICATION_CODE
import kotlinx.android.synthetic.main.fragment_sms_code.sixDigitsCodeLayout
import kotlinx.android.synthetic.main.fragment_sms_code.textError
import kotlinx.android.synthetic.main.fragment_sms_code.theToolbar
import javax.inject.Inject

class SmsCodeFragment : Fragment(R.layout.fragment_sms_code) {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var entranceViewModel: EntranceViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerAuthComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    entranceViewModel = requireActivity().viewModelOf(viewModelFactory) {
      observe(phoneState(), ::handleState)
    }
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    theToolbar.onBackClick {
      popBackStack()
    }
    sixDigitsCodeLayout.afterTextChanged {
      textError.text = ""
    }
    sixDigitsCodeLayout.onDone {
      entranceActivity.onCheckCode(it)
    }
    sixDigitsCodeLayout.setOnClickListener {
      showKeyboard()
      sixDigitsCodeLayout.start()
    }
  }
  
  private fun handleState(state: PhoneAuthState) {
    if (state is Failed && state.exception is FirebaseAuthInvalidCredentialsException) {
      if (state.exception.errorCode == ERROR_INVALID_VERIFICATION_CODE) {
        textError.text = getString(R.string.error_invalid_sms_code)
      }
    }
  }
  
  override fun onResume() {
    super.onResume()
    sixDigitsCodeLayout.start()
  }
}
