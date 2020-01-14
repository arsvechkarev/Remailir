package com.arsvechkarev.remailir.entrance.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.Cancelled
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.Failed
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.OnCheckedAutomatically
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.OnCodeSent
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.UserNotExist
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import core.base.RxViewModel
import core.model.users.User
import firebase.schema.Collections.Users
import log.Loggable
import log.log
import storage.AppUser
import storage.SharedPreferencesManager
import javax.inject.Inject

class EntranceViewModel @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore,
  private val sharedPreferencesManager: SharedPreferencesManager
) : RxViewModel(), Loggable {
  
  private var verificationId: String = ""
  override val logTag = "PhoneAuthorization"
  
  private var _phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = _phoneVerificationState
  
  
  val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      _phoneVerificationState.value = OnCheckedAutomatically
      performMainSignCheck(credential)
      log { "completed, smsCode = ${credential.smsCode}" }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      _phoneVerificationState.value = Failed(exception)
    }
    
    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
      verificationId = p0
      log { "onCodeSent, p0 = $p0" }
      _phoneVerificationState.value = OnCodeSent
    }
  }
  
  fun checkCode(code: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    performMainSignCheck(credential)
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
