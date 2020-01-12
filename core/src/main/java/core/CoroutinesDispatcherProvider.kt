package core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Coroutines dispatcher provider to facilitate tests
 */
interface CoroutinesDispatcherProvider {
  
  val Main: CoroutineDispatcher
  val IO: CoroutineDispatcher
  val Default: CoroutineDispatcher
  
  object DefaultImpl : CoroutinesDispatcherProvider {
    override val Main: CoroutineDispatcher = Dispatchers.Main
    override val IO: CoroutineDispatcher = Dispatchers.IO
    override val Default: CoroutineDispatcher = Dispatchers.Default
  }
}