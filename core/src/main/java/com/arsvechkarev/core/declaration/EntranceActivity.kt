package com.arsvechkarev.core.declaration

import androidx.fragment.app.Fragment

interface EntranceActivity {
  
  fun goToBase()
  
  fun goToSignIn()
  
  fun goToSighUp()
  
  fun goToFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false
  )
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity