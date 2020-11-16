package com.arsvechkarev.core

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsStorage(filename: String, context: Context) : KeyValueStorage {
  
  private val sharedPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
  
  override fun hasString(key: String): Boolean {
    return sharedPrefs.getString(key, null) != null
  }
  
  override fun getString(key: String): String {
    return sharedPrefs.getString(key, null)!!
  }
  
  override fun putString(key: String, value: String) {
    sharedPrefs.edit().putString(key, value).apply()
  }
  
  override fun hasLong(key: String): Boolean {
    return sharedPrefs.getLong(key, Long.MAX_VALUE) != Long.MAX_VALUE
  }
  
  override fun getLong(key: String): Long {
    return sharedPrefs.getLong(key, Long.MAX_VALUE)
  }
  
  override fun putLong(key: String, value: Long) {
    return sharedPrefs.edit().putLong(key, value).apply()
  }
  
  override fun hasInt(key: String): Boolean {
    return sharedPrefs.getInt(key, Int.MAX_VALUE) != Int.MAX_VALUE
  }
  
  override fun getInt(key: String): Int {
    return sharedPrefs.getInt(key, Int.MAX_VALUE)
  }
  
  override fun putInt(key: String, value: Int) {
    return sharedPrefs.edit().putInt(key, value).apply()
  }
  
  override fun execute(block: SharedPreferences.Editor.() -> Unit) {
    val editor = sharedPrefs.edit()
    block(editor)
    editor.apply()
  }
}