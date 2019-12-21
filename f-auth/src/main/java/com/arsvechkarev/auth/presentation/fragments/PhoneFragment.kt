package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.presentation.viewmodels.PhoneViewModel
import com.arsvechkarev.auth.utils.phoneNumber
import com.arsvechkarev.auth.utils.removeDashes
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.declaration.entranceActivity
import com.arsvechkarev.core.model.Country
import com.arsvechkarev.core.model.users.NewUser
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode
import log.Loggable
import log.debug
import javax.inject.Inject

class PhoneFragment : BaseFragment(), Loggable {
  
  override val logTag = "PhoneFragment"
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: PhoneViewModel
  
  override val layout: Int = R.layout.fragment_phone
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
//    viewModel.phoneState().observe(this) {
//
//    }
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
  
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
      FirebaseAuth.getInstance().signInWithCredential(p0).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val user = task.result?.user!!
          FirebaseFirestore.getInstance().collection("Users")
            .document(user.uid)
            .get()
            .addOnCompleteListener {
              if (it.isSuccessful) {
                val userDb = it.result?.toObject(NewUser::class.java)
                if (userDb == null) {
                  childFragmentManager.beginTransaction()
                    .replace(R.id.rootLayoutPhoneFragment, RegistrationFragment())
                    .commit()
                } else {
                  entranceActivity.goToBase()
                }
              }
            }
        }
      }
      debug { "completed, smsCode = ${p0.smsCode}" }
    }
    
    override fun onVerificationFailed(p0: FirebaseException) {
      debug { "completed, fail: $p0" }
    }
    
    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
      debug { "completed, on code sent, p0 = $p0" }
    }
    
    override fun onCodeAutoRetrievalTimeOut(p0: String) {
    
    }
    
  }
  
  fun onCountrySelected(country: Country) {
    textCountryCode.text = "+${country.code}"
  }
  
}
