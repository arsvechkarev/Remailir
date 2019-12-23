package core.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseViewModel : ViewModel(), CoroutineScope {
  
  override val coroutineContext = SupervisorJob() + Dispatchers.Main
  
  private val jobs: MutableList<Job> = ArrayList()
  
  protected fun coroutine(block: suspend CoroutineScope.() -> Unit) {
    jobs.add(launch(coroutineContext) { block() })
  }
  
  fun cancelAllCoroutines() {
    for (job in jobs) {
      job.cancel()
    }
  }
  
  suspend fun cancelAllAndJoin() {
    for (job in jobs) {
      job.cancelAndJoin()
    }
  }
  
  @CallSuper
  override fun onCleared() {
    cancelAllCoroutines()
  }
}