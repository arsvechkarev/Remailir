package com.arsvechkarev.testcommon

import com.arsvechkarev.core.concurrency.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val TestCoroutineDispatcher = TestCoroutineDispatcher()

val FakeDispatchersProvider = object : Dispatchers {
  override val Main = TestCoroutineDispatcher
  override val IO = TestCoroutineDispatcher
  override val Default = TestCoroutineDispatcher
}