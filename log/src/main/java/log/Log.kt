package log

import log.Logger.areDebugMessagesAllowed

object Logger {
  var areDebugMessagesAllowed = false
  
  fun activate() {
    areDebugMessagesAllowed = true
    MyTimber.plant(MyTimber.DebugTree())
  }
  
}

inline fun debug(lazyMessage: () -> String) {
  if (areDebugMessagesAllowed) {
    MyTimber.d(lazyMessage())
  }
}

inline fun debug(lazyMessage: () -> String, e: Exception) {
  if (areDebugMessagesAllowed) {
    MyTimber.d(e, lazyMessage())
  }
}