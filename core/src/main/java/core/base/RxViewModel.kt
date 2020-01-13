package core.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

abstract class RxViewModel : ViewModel() {
  
  private val disposables = ArrayList<Disposable>()
  
  protected fun rxCall(block: () -> Disposable) {
    val disposable = block()
    if (disposable in disposables) disposable.dispose()
    disposables.add(disposable)
  }
  
  override fun onCleared() {
    disposables.forEach { it.dispose() }
    super.onCleared()
  }
}