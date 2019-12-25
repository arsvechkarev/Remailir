package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryAndLettersAdapter
import com.arsvechkarev.auth.utils.CountryCodesHolder.countriesAndCodes
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.popBackStack
import core.viewdelegates.ToolbarSearch
import kotlinx.android.synthetic.main.fragment_country_code.layoutIncludedToolbar
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries

class CountryCodeFragment : BaseFragment(), ToolbarSearch {
  
  override val includedToolbar: View by lazy { layoutIncludedToolbar }
  
  override val layout: Int = R.layout.fragment_country_code
  
  private val adapter = CountryAndLettersAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    adapter.submitList(countriesAndCodes)
    imageSearch.setOnClickListener {
      entranceActivity.goToFragment(CountryCodesSearchFragment(), true)
    }
  }
}
