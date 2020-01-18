package com.arsvechkarev.auth.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.base.RxViewModel
import core.model.users.User
import core.strings.DEFAULT_IMG_URL
import firebase.schema.Collections.Users
import firebase.schema.UserModel
import storage.AppUser
import storage.SharedPreferencesManager
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
  private val firebaseFirestore: FirebaseFirestore,
  private val preferencesManager: SharedPreferencesManager
) : RxViewModel() {
  
  private var userCreationState: MutableLiveData<UserCreationState> = MutableLiveData()
  fun creationState(): LiveData<UserCreationState> = userCreationState
  
  fun createUser(username: String) {
    val firebaseUser = FirebaseAuth.getInstance().currentUser!!
    val newUser = User(firebaseUser.uid, firebaseUser.phoneNumber!!, username, DEFAULT_IMG_URL)
    firebaseFirestore.collection(Users)
      .whereEqualTo(UserModel.username, username)
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
              AppUser.set(newUser, preferencesManager)
              userCreationState.value = UserCreationState.Completed
            }.addOnFailureListener { exception ->
              userCreationState.value = UserCreationState.Failed(exception)
            }
        }
      }
  }
  
}
