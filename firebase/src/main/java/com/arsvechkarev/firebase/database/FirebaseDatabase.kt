package com.arsvechkarev.firebase.database

interface FirebaseDatabase {
  
  suspend fun getList(path: String): MutableList<String>
  
  suspend fun setValues(map: Map<String, Any>)
}