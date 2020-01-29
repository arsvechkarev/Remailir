package com.arsvechkarev.auth.common

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.common.PhoneAuthState.Failed
import com.arsvechkarev.auth.common.PhoneAuthState.OnCodeSent
import com.arsvechkarev.auth.common.PhoneAuthState.Pending
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import core.base.CoroutinesViewModel
import core.strings.SMS_TIMEOUT_SECONDS
import kotlinx.coroutines.delay
import log.Loggable
import log.log
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EntranceViewModel @Inject constructor(
  private val authDelegate: AuthDelegate
) : CoroutinesViewModel(), Loggable {
  
  companion object {
    private const val FAKE_DELAY = 500L
  }
  
  override val logTag = "EntranceViewModel"
  
  private var verificationId: String = ""
  private var enteredPhoneNumber: String = ""
  
  private var _phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = _phoneVerificationState
  
  
  fun verifyPhone(phoneNumber: String, activity: Activity) {
    enteredPhoneNumber = phoneNumber
    PhoneAuthProvider.getInstance()
      .verifyPhoneNumber(phoneNumber, SMS_TIMEOUT_SECONDS, TimeUnit.SECONDS, activity, callbacks)
  }
  
  fun checkCode(code: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    authDelegate.performMainSignCheck(_phoneVerificationState, credential)
  }
  
  fun setPending() {
    _phoneVerificationState.value = Pending
  }
  
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      _phoneVerificationState.value = PhoneAuthState.OnCheckedAutomatically
      authDelegate.performMainSignCheck(_phoneVerificationState, credential)
      log { "completed, smsCode = ${credential.smsCode}" }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      _phoneVerificationState.value = Failed(exception)
    }
    
    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
      this@EntranceViewModel.verificationId = verificationId
      log { "onCodeSent, p0 = $verificationId" }
      
      coroutine {
        delay(FAKE_DELAY)
        _phoneVerificationState.value = OnCodeSent
      }
    }
  }
  
}
