package log

import log.Logger.areDebugMessagesAllowed

object Logger {
  var areDebugMessagesAllowed = false
  
  fun activate() {
    areDebugMessagesAllowed = true
    MyTimber.plant(MyTimber.DebugTree())
  }
  
}

inline fun log(lazyMessage: () -> String?) {
  if (areDebugMessagesAllowed) {
    MyTimber.d(lazyMessage())
  }
}

inline fun log(e: Throwable, lazyMessage: () -> String? = { "" }) {
  if (areDebugMessagesAllowed) {
    MyTimber.d(e, lazyMessage())
  }
}