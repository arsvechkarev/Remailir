package com.arsvechkarev.firebase.database

interface Database {
  
  suspend fun getList(path: String): MutableList<String>
  
  suspend fun setValues(map: Map<String, Any>)
}