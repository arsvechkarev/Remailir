package storage

import android.content.Context
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileStorage(private val context: Context) : Storage {
  
  override suspend fun <T> save(value: T, filename: String) {
    ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE)).use {
      it.writeObject(value)
    }
  }
  
  @Suppress("UNCHECKED_CAST")
  override suspend fun <T> get(filename: String): T? {
    val file = context.getFileStreamPath(filename)
    if (file.exists()) {
      ObjectInputStream(FileInputStream(file)).use {
        return it.readObject() as? T
      }
    }
    return null
  }
  
  override suspend fun delete(filename: String) {
    context.deleteFile(filename)
  }
}