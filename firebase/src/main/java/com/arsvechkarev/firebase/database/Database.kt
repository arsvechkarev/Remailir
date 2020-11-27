package com.arsvechkarev.firebase.database

interface Database {
  
  suspend fun getList(path: String): MutableList<String>
  
  suspend fun setValues(values: Map<String, Any>)
}