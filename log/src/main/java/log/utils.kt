@file:Suppress("RedundantVisibilityModifier")

package log

import timber.log.Timber

public inline fun debug(lazyMessage: () -> String) {
  if (State.isDebugMessagesAllowed) {
    Timber.d(lazyMessage())
  }
}

public inline fun debug(lazyMessage: () -> String, e: Exception) {
  if (State.isDebugMessagesAllowed) {
    Timber.d(e, lazyMessage())
  }
}