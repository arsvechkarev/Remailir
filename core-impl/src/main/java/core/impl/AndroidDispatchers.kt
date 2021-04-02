package core.impl

import core.Dispatchers
import kotlinx.coroutines.Dispatchers as CoroutinesDispatchers

object AndroidDispatchers : Dispatchers {
  
  override val IO = CoroutinesDispatchers.IO
  override val Default = CoroutinesDispatchers.Default
  override val Main = CoroutinesDispatchers.Main.immediate
}