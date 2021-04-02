package core.impl

import android.content.Context
import android.content.SharedPreferences
import core.KeyValueStorage

class SharedPrefsStorage(filename: String, context: Context) : KeyValueStorage {
  
  private val sharedPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
  
  override fun hasString(key: String): Boolean {
    return sharedPrefs.getString(key, null) != null
  }
  
  override fun getString(key: String): String {
    return sharedPrefs.getString(key, null)!!
  }
  
  override fun putString(key: String, value: String) {
    execute { putString(key, value) }
  }
  
  override fun putBoolean(key: String, value: Boolean) {
    execute { putBoolean(key, value) }
  }
  
  override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
    return sharedPrefs.getBoolean(key, defaultValue)
  }
  
  override fun hasLong(key: String): Boolean {
    return sharedPrefs.getLong(key, Long.MAX_VALUE) != Long.MAX_VALUE
  }
  
  override fun getLong(key: String): Long {
    return sharedPrefs.getLong(key, Long.MAX_VALUE)
  }
  
  override fun putLong(key: String, value: Long) {
    execute { putLong(key, value) }
  }
  
  override fun hasInt(key: String): Boolean {
    return sharedPrefs.getInt(key, Int.MAX_VALUE) != Int.MAX_VALUE
  }
  
  override fun getInt(key: String): Int {
    return sharedPrefs.getInt(key, Int.MAX_VALUE)
  }
  
  override fun putInt(key: String, value: Int) {
    execute { putInt(key, value) }
  }
  
  override fun execute(block: SharedPreferences.Editor.() -> Unit) {
    val editor = sharedPrefs.edit()
    block(editor)
    editor.apply()
  }
}