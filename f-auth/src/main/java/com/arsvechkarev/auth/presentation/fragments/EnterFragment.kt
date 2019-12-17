package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.viewmodels.EnterViewModel
import com.arsvechkarev.core.base.BaseFragment
import javax.inject.Inject

class EnterFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: EnterViewModel
  
  override val layout: Int = R.layout.fragment_enter
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    
  }
}