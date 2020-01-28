package com.arsvechkarev.auth.common

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import firebase.schema.Collections
import storage.AppUser
import storage.SharedPreferencesManager
import javax.inject.Inject

class AuthDelegate @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore,
  private val sharedPreferencesManager: SharedPreferencesManager
) {
  
  fun performMainSignCheck(
    state: MutableLiveData<PhoneAuthState>,
    credential: PhoneAuthCredential
  ) {
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
      if (!signInTask.isSuccessful) {
        state.value = PhoneAuthState.Failed(signInTask.exception)
        return@addOnCompleteListener
      }
      val user = signInTask.result!!.user!!
      firebaseFirestore.collection(Collections.Users)
        .document(user.uid)
        .get()
        .addOnCompleteListener { fetchUserTask ->
          if (fetchUserTask.isSuccessful) {
            val userFromDb = fetchUserTask.result?.toObject(User::class.java)
            if (userFromDb == null) {
              state.value = PhoneAuthState.UserNotExist
            } else {
              AppUser.set(userFromDb, sharedPreferencesManager)
              state.value = PhoneAuthState.UserAlreadyExists
            }
          }
        }.addOnFailureListener {
          state.value = PhoneAuthState.Failed(it)
        }
    }.addOnFailureListener {
      state.value = PhoneAuthState.Failed(it)
    }.addOnCanceledListener {
      state.value = PhoneAuthState.Cancelled
    }
  }
  
  
}