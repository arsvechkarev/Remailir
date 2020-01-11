package core.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

abstract class RxViewModel : ViewModel() {
  
  private val disposables = ArrayList<Disposable>()
  
  protected fun rxCall(block: () -> Disposable) {
    disposables.add(block())
  }
  
  override fun onCleared() {
    disposables.forEach { it.dispose() }
    super.onCleared()
  }
}