package com.arsvechkarev.core

import androidx.annotation.CallSuper
import com.arsvechkarev.core.concurrency.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<V : MvpView>(
  dispatchers: Dispatchers
) : MvpPresenter<V>() {
  
  private val scope = CoroutineScope(SupervisorJob() + dispatchers.Main)
  
  protected fun coroutine(block: suspend () -> Unit) {
    scope.launch {
      withTimeout(REQUEST_TIMEOUT) { block() }
    }
  }
  
  protected inline fun updateView(block: V.() -> Unit) {
    viewState.apply(block)
  }
  
  @CallSuper
  override fun onDestroy() {
    scope.cancel()
  }
}