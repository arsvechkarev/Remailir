package com.arsvechkarev.auth.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.utils.normalize
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.NewUser
import com.arsvechkarev.firebase.Collections.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import log.Loggable
import log.debug
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class PhoneViewModel @Inject constructor(
  private val phoneAuthProvider: PhoneAuthProvider,
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel(), Loggable {
  
  override val logTag = "PhoneAuthorization"
  
  
  private var phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = phoneVerificationState
  
  
  fun sendNumber(countryCode: String, phoneNumber: String, activity: Activity) {
    val phone = countryCode + phoneNumber.normalize()
    debug { "phone = $phone" }
    phoneAuthProvider.verifyPhoneNumber(phone, 60, SECONDS, activity, callbacks)
  }
  
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      debug { "completed, credential = $credential" }
      firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
        val user = signInTask.result!!.user!!
        firebaseFirestore.collection(Users)
          .document(user.uid)
          .get()
          .addOnCompleteListener { fetchUserTask ->
            if (fetchUserTask.isSuccessful) {
              val userDb = fetchUserTask.result?.toObject(NewUser::class.java)
              if (userDb == null) {
                phoneVerificationState.value = PhoneAuthState.UserNotExist
              } else {
                phoneVerificationState.value = PhoneAuthState.UserAlreadyExists
              }
            }
          }.addOnFailureListener {
            phoneVerificationState.value = PhoneAuthState.Failed(it)
            debug(it) { "fail1" }
          }
      }.addOnFailureListener {
        phoneVerificationState.value = PhoneAuthState.Failed(it)
        debug(it) { "fail1" }
      }.addOnCanceledListener {
        phoneVerificationState.value = PhoneAuthState.Cancelled
      }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      debug(exception) { "fail2" }
      phoneVerificationState.value = PhoneAuthState.Failed(exception)
    }
    
  }
}