package com.arsvechkarev.core.extenstions

inline fun <T : Any> T?.ifNotNull(block: (T) -> Unit) {
  this?.apply(block)
}

inline fun <T : Any> T.ifTrue(condition: (T) -> Boolean, block: T.() -> Unit) {
  if (condition(this)) apply(block)
}