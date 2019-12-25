package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.list.CountryAndLettersAdapter
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.viewModelOf
import core.recycler.DisplayableItem
import core.viewdelegates.ToolbarSearch
import kotlinx.android.synthetic.main.fragment_country_code.layoutIncludedToolbar
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries
import javax.inject.Inject

class CountriesFragment : BaseFragment(), ToolbarSearch {
  
  override val includedToolbar: View by lazy { layoutIncludedToolbar }
  
  override val layout: Int = R.layout.fragment_country_code
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy {
    viewModelOf<CountriesViewModel>(viewModelFactory) {
      observe(countriesAndCodes, ::handleList)
    }
  }
  
  private val adapter = CountryAndLettersAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.fetchCountriesAndCodes()
    textTitle.text = getString(R.string.title_countries)
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    imageBack.setOnClickListener {
      popBackStack()
    }
    imageSearch.setOnClickListener {
      entranceActivity.goToFragment(SearchCountryFragment(), true)
    }
  }
  
  private fun handleList(countries: MutableList<DisplayableItem>) {
    adapter.submitList(countries)
  }
}
