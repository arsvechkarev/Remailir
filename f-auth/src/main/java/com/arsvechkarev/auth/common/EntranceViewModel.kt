package com.arsvechkarev.auth.common

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.common.PhoneAuthState.Cancelled
import com.arsvechkarev.auth.common.PhoneAuthState.Failed
import com.arsvechkarev.auth.common.PhoneAuthState.OnCodeSent
import com.arsvechkarev.auth.common.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.auth.common.PhoneAuthState.UserNotExist
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import core.base.CoroutinesViewModel
import core.model.users.User
import firebase.schema.Collections.Users
import kotlinx.coroutines.delay
import log.Loggable
import log.log
import storage.AppUser
import storage.SharedPreferencesManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EntranceViewModel @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore,
  private val sharedPreferencesManager: SharedPreferencesManager
) : CoroutinesViewModel(), Loggable {
  
  companion object {
    private const val DELAY_DURATION = 700L
  }
  
  override val logTag = "EntranceViewModel"
  
  private var verificationId: String = ""
  private var enteredPhoneNumber: String = ""
  
  private var _phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = _phoneVerificationState
  
  
  fun verifyPhone(phoneNumber: String, activity: Activity) {
    enteredPhoneNumber = phoneNumber
    PhoneAuthProvider.getInstance()
      .verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, callbacks)
  }
  
  fun checkCode(code: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    performMainSignCheck(credential)
  }
  
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      _phoneVerificationState.value = PhoneAuthState.OnCheckedAutomatically
      performMainSignCheck(credential)
      log { "completed, smsCode = ${credential.smsCode}" }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      _phoneVerificationState.value = Failed(exception)
    }
    
    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
      this@EntranceViewModel.verificationId = verificationId
      log { "onCodeSent, p0 = $verificationId" }
      
      coroutine {
        delay(DELAY_DURATION)
        _phoneVerificationState.value = OnCodeSent
      }
    }
  }
  
  private fun performMainSignCheck(credential: PhoneAuthCredential) {
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
      if (!signInTask.isSuccessful) {
        _phoneVerificationState.value = Failed(signInTask.exception)
        return@addOnCompleteListener
      }
      val user = signInTask.result!!.user!!
      firebaseFirestore.collection(Users)
        .document(user.uid)
        .get()
        .addOnCompleteListener { fetchUserTask ->
          if (fetchUserTask.isSuccessful) {
            val userFromDb = fetchUserTask.result?.toObject(User::class.java)
            if (userFromDb == null) {
              _phoneVerificationState.value = UserNotExist
            } else {
              AppUser.set(userFromDb, sharedPreferencesManager)
              _phoneVerificationState.value = UserAlreadyExists(userFromDb)
            }
          }
        }.addOnFailureListener {
          _phoneVerificationState.value = Failed(it)
        }
    }.addOnFailureListener {
      _phoneVerificationState.value = Failed(it)
    }.addOnCanceledListener {
      _phoneVerificationState.value = Cancelled
    }
  }
  
}
