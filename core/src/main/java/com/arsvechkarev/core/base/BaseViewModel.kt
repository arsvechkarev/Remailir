package com.arsvechkarev.core.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin

abstract class BaseViewModel : ViewModel(), CoroutineScope {
  
  override val coroutineContext = SupervisorJob() + Dispatchers.Main
  
  val jobs: MutableList<Job> = ArrayList()
  
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