package firebase

import androidx.lifecycle.ViewModel

abstract class FireViewModel<S>(
  private val fireRepository: SnapshotFireRepository<S>
) : ViewModel() {
  
  override fun onCleared() {
    fireRepository.clear()
  }
}
