package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.list.CountryAndLettersAdapter
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Default
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Searching
import com.arsvechkarev.views.SpringItemAnimator
import core.base.BaseFragment
import core.base.entranceActivity
import core.di.coreComponent
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
import javax.inject.Inject


class CountriesFragment : BaseFragment() {
  
  private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
  
    override fun handleOnBackPressed() {
      if (theToolbar.isInSearchMode) {
        theToolbar.goToNormalMode()
        viewModel.fetchAll()
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
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerAuthComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    callback.isEnabled = false
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setupToolbar()
    handleInitialStateState(viewModel.currentState)
    progressBarCountries.visible()
    recyclerCountries.setupWith(adapter)
    recyclerCountries.itemAnimator = SpringItemAnimator()
  }
  
  override fun onResume() {
    super.onResume()
    if (theToolbar.isInSearchMode) {
      theToolbar.editTextSearch.requestFocus()
      showKeyboard()
    }
  }
  
  private fun handleInitialStateState(state: CountriesViewModel.State) {
    when (state) {
      is Default -> viewModel.fetchAll()
      is Searching -> {
        theToolbar.goToSearchMode()
        viewModel.searchCountries(state.currentText)
      }
    }
  }
  
  private fun setupToolbar() {
    theToolbar.attachLifecycle(this)
    theToolbar.setTextWatcher {
      viewModel.searchCountries(it)
    }
    theToolbar.onBackClick {
      hideKeyboard(theToolbar.editTextSearch)
      popBackStack()
    }
    theToolbar.onSearchAction {
      viewModel.currentState = Searching()
      theToolbar.goToSearchMode(onEnd = {
        showKeyboard()
        callback.isEnabled = true
      })
    }
  }
  
  private fun handleList(countries: List<DisplayableItem>) {
    adapter.submitList(countries)
    progressBarCountries.gone()
  }
}
