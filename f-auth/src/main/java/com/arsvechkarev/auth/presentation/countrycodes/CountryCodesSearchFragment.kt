package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesBaseAdapter
import com.arsvechkarev.auth.utils.CountryCodesHolder
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.popBackStack
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries

class CountryCodesSearchFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_country_code_search
  
  private val adapter = CountryCodesBaseAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    adapter.submitList(CountryCodesHolder.countryCodes)
  }
}