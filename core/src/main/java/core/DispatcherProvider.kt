package core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Coroutines dispatcher provider to facilitate tests
 */
interface DispatcherProvider {
  
  val Main: CoroutineDispatcher
  val IO: CoroutineDispatcher
  val Default: CoroutineDispatcher
  
  object DefaultImpl : DispatcherProvider {
    override val Main: CoroutineDispatcher = Dispatchers.Main
    override val IO: CoroutineDispatcher = Dispatchers.IO
    override val Default: CoroutineDispatcher = Dispatchers.Default
  }
}