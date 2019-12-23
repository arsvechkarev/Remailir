package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesAdapter
import com.arsvechkarev.auth.utils.getCountries
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.popBackStack
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries

class CountryCodeFragment: BaseFragment() {
  
  override val layout: Int = R.layout.fragment_country_code
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    adapter.submitList(getCountries())
  }
}
