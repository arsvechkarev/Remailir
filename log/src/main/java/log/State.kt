package log

import timber.log.Timber

object State {
  var isDebugMessagesAllowed = false
}

fun configureLog(debug: Boolean = true) {
  State.isDebugMessagesAllowed = debug
  if (debug) {
    Timber.plant(Timber.DebugTree())
  }
}