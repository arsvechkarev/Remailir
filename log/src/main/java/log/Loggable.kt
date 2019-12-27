package log

import android.util.Log

/**
 * Facilitating logging for classes
 */
interface Loggable {
  
  val logTag: String
}

fun Loggable.log(lazyMessage: () -> Any) {
  if (Logger.areDebugMessagesAllowed) {
    Log.d(logTag, lazyMessage().toString())
  }
}

fun Loggable.log(exception: Exception, lazyMessage: () -> Any) {
  if (Logger.areDebugMessagesAllowed) {
    Log.d(logTag, lazyMessage().toString(), exception)
  }
}