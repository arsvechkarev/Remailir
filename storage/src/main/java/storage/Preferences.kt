package storage

interface Preferences {
  
  fun saveString(key: String, value: String)
  
  fun getString(key: String, defaultValue: String): String
  
  fun getString(key: String): String?
  
  fun isExist(key: String): Boolean {
    return getString(key) != null
  }
}