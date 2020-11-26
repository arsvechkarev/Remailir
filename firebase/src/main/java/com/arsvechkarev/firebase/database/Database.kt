package com.arsvechkarev.firebase.database

interface Database {
  
  suspend fun <T> getList(path: String, mapper: (String) -> T): MutableList<T>
  
  suspend fun setValues(values: Map<String, Any>)
}