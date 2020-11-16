package com.arsvechkarev.core.concurrency

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Dispatchers for coroutines
 */
interface Dispatchers {
  
  val IO: CoroutineDispatcher
  val Default: CoroutineDispatcher
  val Main: CoroutineDispatcher
}