package com.arsvechkarev.remailir.main.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.NewUser
import com.arsvechkarev.firebase.Collections.Users
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.Cancelled
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.Failed
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.OnCheckedAutomatically
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.OnCodeSent
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.UserNotExist
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import log.Loggable
import log.debug
import javax.inject.Inject

class EntranceViewModel @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel(), Loggable {
  
  private var verificationId: String = ""
  override val logTag = "PhoneAuthorization"
  
  private var phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = phoneVerificationState
  
  
  val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      phoneVerificationState.value = OnCheckedAutomatically
      performMainSignCheck(credential)
      debug { "completed, smsCode = ${credential.smsCode}" }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      phoneVerificationState.value = Failed(exception)
    }
    
    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
      verificationId = p0
      phoneVerificationState.value = OnCodeSent
      debug { "onCodeSent, p0 = $p0" }
    }
  }
  
  fun checkCode(code: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    performMainSignCheck(credential)
  }
  
  private fun performMainSignCheck(credential: PhoneAuthCredential) {
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
      val user = signInTask.result!!.user!!
      firebaseFirestore.collection(Users)
        .document(user.uid)
        .get()
        .addOnCompleteListener { fetchUserTask ->
          if (fetchUserTask.isSuccessful) {
            val userDb = fetchUserTask.result?.toObject(NewUser::class.java)
            if (userDb == null) {
              phoneVerificationState.value = UserNotExist
            } else {
              phoneVerificationState.value = UserAlreadyExists
            }
          }
        }.addOnFailureListener {
          phoneVerificationState.value = Failed(it)
          debug(it) { "fail1" }
        }
    }.addOnFailureListener {
      phoneVerificationState.value = Failed(it)
    }.addOnCanceledListener {
      phoneVerificationState.value = Cancelled
    }
  }
  
}
