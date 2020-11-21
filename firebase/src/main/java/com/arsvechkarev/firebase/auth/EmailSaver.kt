package com.arsvechkarev.firebase.auth

interface EmailSaver {
  
  fun getEmail(): String?
  
  fun saveEmail(email: String)
  
  fun deleteEmailSynchronously()
}