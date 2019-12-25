package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesAdapter
import com.arsvechkarev.auth.utils.CountryCodesHolder
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.popBackStack
import core.extensions.showKeyboard
import core.viewdelegates.LayoutSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.layoutIncludedSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.recyclerCountries

class CountryCodesSearchFragment : BaseFragment(), LayoutSearch {
  
  override val includedSearch: View by lazy { layoutIncludedSearch }
  
  override val layout: Int = R.layout.fragment_country_code_search
  
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    adapter.submitList(CountryCodesHolder.countries)
  }
  
  override fun onResume() {
    super.onResume()
    editTextSearch.requestFocus()
    showKeyboard()
  }
}