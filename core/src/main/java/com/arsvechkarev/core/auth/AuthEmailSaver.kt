package com.arsvechkarev.core.auth

interface AuthEmailSaver {
  
  fun saveEmail(email: String)
  
  fun getEmail(): String?
}