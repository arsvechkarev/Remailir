package com.arsvechkarev.core.extenstions

fun <T : Any> T?.ifNotNull(block: (T) -> Unit) {
  this?.apply(block)
}