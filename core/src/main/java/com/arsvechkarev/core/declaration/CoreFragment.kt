package com.arsvechkarev.core.declaration

import androidx.fragment.app.Fragment

interface CoreFragment {
  
  fun addFragment(fragment: Fragment, addToBackStack: Boolean = false)
  
  fun switchFragment(fragment: Fragment, addToBackStack: Boolean = false)
}