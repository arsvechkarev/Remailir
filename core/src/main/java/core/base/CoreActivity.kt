package core.base

import androidx.fragment.app.Fragment

interface CoreActivity {
  
  fun goToFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    animate: Boolean = false
  )
  
  fun signOut()
}

val Fragment.coreActivity: CoreActivity
  get() = activity as CoreActivity