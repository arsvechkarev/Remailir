package core.declaration

import androidx.fragment.app.Fragment

interface CoreActivity {
  
  fun goToFragmentFromRoot(
    fragment: Fragment,
    addToBackStack: Boolean = false
  )
  
  fun signOut()
}

val Fragment.coreActivity: CoreActivity
  get() = activity as CoreActivity