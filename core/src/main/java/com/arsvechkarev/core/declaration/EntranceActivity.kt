package com.arsvechkarev.core.declaration

import androidx.fragment.app.Fragment
import com.arsvechkarev.core.model.Country

interface EntranceActivity {
  
  fun goToBase()
  
  fun onCountrySelected(country: Country)
  
  fun goToFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false
  )
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity