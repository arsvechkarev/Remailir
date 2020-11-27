package com.arsvechkarev.testcommon

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import com.arsvechkarev.core.concurrency.Dispatchers as MyDispatchers

@OptIn(ExperimentalCoroutinesApi::class)
val TestCoroutineDispatcher = TestCoroutineDispatcher()

val FakeDispatchersProvider = object : MyDispatchers {
  override val Main = TestCoroutineDispatcher
  override val IO = TestCoroutineDispatcher
  override val Default = TestCoroutineDispatcher
}