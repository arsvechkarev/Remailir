package log

import timber.log.Timber

object State {
  var isDebugMessagesAllowed = false
}

fun activate() {
  State.isDebugMessagesAllowed = true
  MyTimber.plant(MyTimber.DebugTree())
}

inline fun debug(lazyMessage: () -> String) {
  if (State.isDebugMessagesAllowed) {
    Timber.d(lazyMessage())
  }
}

inline fun debug(lazyMessage: () -> String, e: Exception) {
  if (State.isDebugMessagesAllowed) {
    Timber.d(e, lazyMessage())
  }
}