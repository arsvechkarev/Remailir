package log

import android.util.Log

interface Loggable {
  
  val logTag: String
}

fun Loggable.debug(lazyMessage: () -> Any) {
  if (Logger.areDebugMessagesAllowed) {
    Log.d(logTag, lazyMessage().toString())
  }
}