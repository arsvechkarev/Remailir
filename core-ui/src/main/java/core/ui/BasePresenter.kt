package core.ui

import androidx.annotation.CallSuper
import core.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<V : MvpView>(
  protected val dispatchers: Dispatchers
) : MvpPresenter<V>() {
  
  protected val scope = CoroutineScope(SupervisorJob() + dispatchers.Main)
  
  protected fun coroutine(block: suspend () -> Unit) {
    scope.launch {
      withTimeout(REQUEST_TIMEOUT) { block() }
    }
  }
  
  @CallSuper
  override fun onDestroy() {
    scope.cancel()
  }
}