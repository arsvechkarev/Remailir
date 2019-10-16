package com.arsvechkarev.signin.repositories

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignInRepository @Inject constructor() {
  
  private lateinit var email: String
  private lateinit var password: String
  
  private lateinit var onSuccess: () -> Unit
  private lateinit var onFailure: (Throwable) -> Unit
  
  fun setParams(
    email: String,
    password: String
  ): SignInRepository {
    this.email = email
    this.password = password
    return this
  }
  
  fun onSuccess(block: () -> Unit): SignInRepository {
    onSuccess = block
    return this
  }
  
  fun onFailure(block: (Throwable) -> Unit): SignInRepository {
    onFailure = block
    return this
  }
  
  fun execute() {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
      .addOnFailureListener { onFailure(it) }
      .addOnSuccessListener { onSuccess() }
  }
}