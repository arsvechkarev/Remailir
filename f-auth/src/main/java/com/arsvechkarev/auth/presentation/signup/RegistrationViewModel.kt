package com.arsvechkarev.auth.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.base.BaseViewModel
import core.model.users.User
import core.strings.DEFAULT_IMG_URL
import firebase.Collections.Users
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
  private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {
  
  private var userCreationState: MutableLiveData<UserCreationState> = MutableLiveData()
  fun creationState(): LiveData<UserCreationState> = userCreationState
  
  fun createUser(username: String) {
    val firebaseUser = FirebaseAuth.getInstance().currentUser!!
    val newUser = User(firebaseUser.uid, firebaseUser.phoneNumber!!, username, DEFAULT_IMG_URL)
    firebaseFirestore.collection(Users)
      .whereEqualTo("name", username)
      .get()
      .addOnCompleteListener {
        val foundUsers = it.result?.toObjects(User::class.java)
        if (foundUsers?.size != 0) {
          userCreationState.value = UserCreationState.NameOccupied
        } else {
          firebaseFirestore.collection(Users)
            .document(firebaseUser.uid)
            .set(newUser)
            .addOnCompleteListener {
              userCreationState.value = UserCreationState.Completed
            }.addOnFailureListener { exception ->
              userCreationState.value = UserCreationState.Failed(exception)
            }
        }
      }
  }
  
}
