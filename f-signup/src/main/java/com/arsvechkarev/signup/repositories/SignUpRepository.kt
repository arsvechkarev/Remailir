package com.arsvechkarev.signup.repositories

import com.arsvechkarev.core.model.users.User
import com.arsvechkarev.firebase.Collections.Users
import com.arsvechkarev.firebase.DEFAULT_IMG_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SignUpRepository @Inject constructor() {
  
  private lateinit var username: String
  private lateinit var email: String
  private lateinit var password: String
  private lateinit var onSuccess: () -> Unit
  private lateinit var onFailure: (Throwable) -> Unit
  
  fun setParams(
    username: String,
    email: String,
    password: String
  ): SignUpRepository {
    this.username = username
    this.email = email
    this.password = password
    return this
  }
  
  fun onSuccess(block: () -> Unit): SignUpRepository {
    onSuccess = block
    return this
  }
  
  fun onFailure(block: (Throwable) -> Unit): SignUpRepository {
    onFailure = block
    return this
  }
  
  fun execute() {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
      .addOnSuccessListener { createUserInDatabase() }
      .addOnFailureListener { onFailure(it) }
  }
  
  private fun createUserInDatabase() {
    val user = User(
      FirebaseAuth.getInstance().uid!!, username, email, password, DEFAULT_IMG_URL
    )
    FirebaseFirestore.getInstance()
      .collection(Users)
      .document(user.id)
      .set(user)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener { onFailure(it) }
  }
}