package firebase

import com.google.firebase.firestore.ListenerRegistration

abstract class SnapshotFireRepository<S> {
  
  protected var listener: SnapshotListener<S>? = null
  private var listenerRegistration: ListenerRegistration? = null
  
  fun registration(block: () -> ListenerRegistration) {
    listenerRegistration = block.invoke()
  }
  
  fun clear() {
    listener = null
    listenerRegistration?.remove()
    listenerRegistration = null
  }
}