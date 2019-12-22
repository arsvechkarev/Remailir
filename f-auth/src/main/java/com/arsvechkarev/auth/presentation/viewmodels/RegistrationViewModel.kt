package com.arsvechkarev.auth.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.NewUser
import com.arsvechkarev.firebase.Collections.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {
  
  private var userCreationState: MutableLiveData<UserCreationState> = MutableLiveData()
  
  fun createUser(username: String, password: String) {
    val firebaseUser = FirebaseAuth.getInstance().currentUser!!
    val newUser = NewUser(firebaseUser.uid, firebaseUser.phoneNumber!!, username, password, "")
    firebaseFirestore.collection(Users)
      .document(firebaseUser.uid)
      .set(newUser)
      .addOnCompleteListener {
        userCreationState.value = UserCreationState.Completed
      }.addOnFailureListener {
        userCreationState.value = UserCreationState.Failed(it)
      }
  }
  
}
