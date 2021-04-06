package com.arsvechkarev.testcommon

import core.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val TestCoroutineDispatcher = TestCoroutineDispatcher()

val FakeDispatchers = object : Dispatchers {
  override val Main = TestCoroutineDispatcher
  override val IO = TestCoroutineDispatcher
  override val Default = TestCoroutineDispatcher
}