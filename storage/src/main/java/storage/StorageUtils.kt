package storage

import android.content.Context
import core.strings.FILENAME_USER

object StorageUtils {
  
  suspend fun deleteAll(context: Context) {
    val storage = FileStorage(context)
    storage.delete(FILENAME_USER)
  }
}