package com.arsvechkarev.firebase.database

interface Database {
  
  suspend fun saveUser(username: String, email: String)
}