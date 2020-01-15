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
import core.di.coreComponent
import core.di.modules.ContextModule
import core.extensions.gone
import core.extensions.observe
import core.extensions.viewModelOf
import core.extensions.visible
import core.recycler.DisplayableItem
import kotlinx.android.synthetic.main.fragment_country_code.progressBarCountries
import kotlinx.android.synthetic.main.fragment_country_code.recyclerCountries
import kotlinx.android.synthetic.main.fragment_country_code.theToolbar
import styles.COLOR_ACCENT
import javax.inject.Inject


class CountriesFragment : BaseFragment() {
  
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
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.builder()
      .coreComponent(coreComponent)
      .contextModule(ContextModule(context!!))
      .build()
      .inject(this)
    viewModel.fetchCountriesAndCodes()
    progressBarCountries.visible()
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    theToolbar.onBackClick {
      //      popBackStack()
      theToolbar.reverseWave()
    }
    theToolbar.onSearchClick {
      theToolbar.goToSearchMode(COLOR_ACCENT)
      //      entranceActivity.goToFragment(SearchCountryFragment(), true)
    }
  }
  
  private fun handleList(countries: MutableList<DisplayableItem>) {
    adapter.submitList(countries)
    progressBarCountries.gone()
  }
}
