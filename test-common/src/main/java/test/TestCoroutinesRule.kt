package test

import core.providers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestCoroutinesRule(
  val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {
  
  val fakeDispatcherProvider = object : DispatcherProvider {
    override val Main: CoroutineDispatcher = testCoroutineDispatcher
    override val IO: CoroutineDispatcher = testCoroutineDispatcher
    override val Default: CoroutineDispatcher = testCoroutineDispatcher
  }
  
  override fun starting(description: Description?) {
    super.starting(description)
    Dispatchers.setMain(testCoroutineDispatcher)
  }
  
  override fun finished(description: Description?) {
    super.finished(description)
    Dispatchers.resetMain()
    testCoroutineDispatcher.cleanupTestCoroutines()
  }
  
}