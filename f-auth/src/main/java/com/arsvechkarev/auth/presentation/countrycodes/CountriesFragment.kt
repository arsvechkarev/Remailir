package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.list.CountryAndLettersAdapter
import com.arsvechkarev.views.SpringItemAnimator
import core.base.BaseFragment
import core.base.entranceActivity
import core.di.coreComponent
import core.di.modules.ContextModule
import core.extensions.gone
import core.extensions.hideKeyboard
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.setupWith
import core.extensions.showKeyboard
import core.extensions.viewModelOf
import core.extensions.visible
import core.recycler.DisplayableItem
import kotlinx.android.synthetic.main.fragment_countries.progressBarCountries
import kotlinx.android.synthetic.main.fragment_countries.recyclerCountries
import kotlinx.android.synthetic.main.fragment_countries.theToolbar
import styles.COLOR_ACCENT
import styles.COLOR_BACKGROUND
import javax.inject.Inject


class CountriesFragment : BaseFragment() {
  
  private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      if (theToolbar.isInSearchMode) {
        theToolbar.goToNormalMode()
        isEnabled = false
      }
    }
  }
  
  override val layout: Int = R.layout.fragment_countries
  
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
    recyclerCountries.setupWith(adapter)
    recyclerCountries.itemAnimator = SpringItemAnimator()
    requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    callback.isEnabled = false
    setupToolbar()
  }
  
  private fun setupToolbar() {
    theToolbar.onBackClick {
      theToolbar.exitSearchIfNeeded()
      hideKeyboard(theToolbar.editTextSearch)
      popBackStack()
    }
    theToolbar.onSearchTextChanged {
      viewModel.filter(it)
    }
    theToolbar.onSearchAction {
      theToolbar.goToSearchMode(COLOR_ACCENT, COLOR_BACKGROUND) {
        showKeyboard()
        callback.isEnabled = true
      }
    }
  }
  
  private fun handleList(countries: List<DisplayableItem>) {
    adapter.submitList(countries)
    progressBarCountries.gone()
  }
}
