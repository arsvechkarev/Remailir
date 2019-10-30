package com.arsvechkarev.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Represents a storage that can manipulate some data
 */
interface Storage {
  
  /**
   * Save value to the file with name [filename]
   */
  suspend fun <T> save(value: T, filename: String)
  
  /**
   * Returns something by given [filename] (or null if it not exists)
   */
  suspend fun <T> get(filename: String): T?
  
  /**
   * Deletes something by [filename]
   */
  suspend fun delete(filename: String)
  
  fun <T> saveSwitching(value: T, filename: String) {
    GlobalScope.launch(Dispatchers.IO) {
      save(value, filename)
    }
  }
}
