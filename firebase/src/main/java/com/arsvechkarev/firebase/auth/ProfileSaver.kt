package com.arsvechkarev.firebase.auth

interface ProfileSaver {
  
  fun getEmail(): String?
  
  fun getUsername(): String?
  
  fun saveEmail(email: String)
  
  fun saveUsername(username: String)
}