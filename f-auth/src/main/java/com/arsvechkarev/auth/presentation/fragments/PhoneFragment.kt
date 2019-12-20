package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.viewmodels.PhoneViewModel
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.model.Country
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhoneFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: PhoneViewModel
  
  override val layout: Int = R.layout.fragment_phone
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
      override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        val digits = s?.toString()?.replace(Regex("[(\\-) ]"), "")?.length ?: 0
        buttonNext.isEnabled = digits >= 10
      }
    })
    
    buttonNext.setOnClickListener {
      PhoneAuthProvider.getInstance().verifyPhoneNumber(
        textCountryCode.text.toString() + editTextPhone.string().replace(Regex("[(\\-) ]"),
          ""), // Phone number to verify
        60, // Timeout duration
        TimeUnit.SECONDS, // Unit of timeout
        activity!!, // Activity (for callback binding)
        Callbacks) // OnVerificationStateChangedCallbacks
      
    }
  }
  
  object Callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
      p0.smsCode
    }
    
    override fun onVerificationFailed(p0: FirebaseException) {
    
    }
    
    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
    }
    
    override fun onCodeAutoRetrievalTimeOut(p0: String) {
    
    }
    
  }
  
  fun onCountrySelected(country: Country) {
    textCountryCode.text = "+${country.code}"
  }
  
}
