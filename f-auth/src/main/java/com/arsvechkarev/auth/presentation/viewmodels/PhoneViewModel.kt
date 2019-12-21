package com.arsvechkarev.auth.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.NewUser
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class PhoneViewModel @Inject constructor(
  private val phoneAuthProvider: PhoneAuthProvider,
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {
  
  private var phoneVerificationState: MutableLiveData<PhoneAuthState> = MutableLiveData()
  
  fun phoneState(): LiveData<PhoneAuthState> = phoneVerificationState
  
  
  fun sendNumber(countryCode: String, phoneNumber: String, activity: Activity) {
    val phone = countryCode + phoneNumber
    phoneAuthProvider.verifyPhoneNumber(phone, 60, SECONDS, activity, callbacks)
  }
  
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
        val user = signInTask.result!!.user!!
        firebaseFirestore.collection("Users")
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
          }
      }.addOnFailureListener {
        phoneVerificationState.value = PhoneAuthState.Failed(it)
      }.addOnCanceledListener {
        phoneVerificationState.value = PhoneAuthState.Cancelled
      }
    }
    
    override fun onVerificationFailed(exception: FirebaseException) {
      phoneVerificationState.value = PhoneAuthState.Failed(exception)
    }
    
  }
}
