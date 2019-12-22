package com.arsvechkarev.auth.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.Completed
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.Failed
import com.arsvechkarev.auth.presentation.viewmodels.SignInState.IncorrectPassword
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.NewUser
import com.arsvechkarev.firebase.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import log.Loggable
import log.debug
import javax.inject.Inject

class SignInViewModel @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel(), Loggable {
  
  override val logTag = "SignInViewModel"
  
  private val signInState: MutableLiveData<SignInState> = MutableLiveData()
  
  fun signInState(): LiveData<SignInState> = signInState
  
  fun signIn(password: String) {
    firebaseFirestore.collection(Collections.Users)
      .document(firebaseAuth.currentUser!!.uid)
      .get()
      .addOnCompleteListener {
        if (it.isSuccessful) {
          val userDb = it.result!!.toObject(NewUser::class.java)
          debug { "enteredPassword = $password" }
          debug { "dbPassword = ${userDb!!.password}" }
          if (userDb!!.password == password) {
            signInState.value = Completed
          } else {
            signInState.value = IncorrectPassword
          }
        }
      }.addOnFailureListener {
        signInState.value = Failed(it)
      }
  }
  
}