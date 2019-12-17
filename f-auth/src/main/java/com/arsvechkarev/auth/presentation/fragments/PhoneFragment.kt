package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.viewmodels.PhoneViewModel
import com.arsvechkarev.core.base.BaseFragment
import javax.inject.Inject

class PhoneFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: PhoneViewModel
  
  override val layout: Int = R.layout.fragment_profile
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  
  }
}
