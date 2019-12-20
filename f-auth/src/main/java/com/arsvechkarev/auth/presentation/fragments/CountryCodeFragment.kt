package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesAdapter
import com.arsvechkarev.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries
import javax.inject.Inject

class CountryCodeFragment: BaseFragment() {
  
  override val layout: Int = R.layout.fragment_country_code
  private val adapter = CountryCodesAdapter {
    (parentFragment as PhoneFragment)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
  }
}
