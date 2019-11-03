package com.arsvechkarev.core.declaration

import androidx.fragment.app.Fragment

interface EntranceActivity {
  
  fun goToBase()
  
  fun goToSignIn()
  
  fun goToSighUp()
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity