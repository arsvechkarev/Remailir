package com.arsvechkarev.remailir.additional


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arsvechkarev.remailir.R

class SettingsFragment : Fragment() {
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_settings, container, false)
  }
  
  
}
