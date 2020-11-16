package com.arsvechkarev.core.concurrency

import kotlinx.coroutines.Dispatchers as CoroutinesDispatchers

object AndroidSchedulers : Dispatchers {
  
  override val IO = CoroutinesDispatchers.IO
  override val Default = CoroutinesDispatchers.Default
  override val Main = CoroutinesDispatchers.Main
}