package core.base

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration

abstract class FireViewModel : ViewModel() {
  
  private val registrations = ArrayList<ListenerRegistration>()
  
  fun register(registration: ListenerRegistration) {
    registrations.add(registration)
  }
  
  fun clear() {
    registrations.forEach { it.remove() }
  }
}