package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.list.CountryCodesAdapter
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.hideKeyboard
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.showKeyboard
import core.extensions.viewModelOf
import core.model.other.Country
import core.viewdelegates.LayoutSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.layoutIncludedSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.recyclerCountries
import javax.inject.Inject

class SearchCountryFragment : BaseFragment(), LayoutSearch {
  
  override val includedSearch: View by lazy { layoutIncludedSearch }
  override val layout: Int = R.layout.fragment_country_code_search
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy {
    viewModelOf<SearchCountryViewModel>(viewModelFactory) {
      observe(allCountries, ::handleAllList)
      observe(filteredCountries, ::handleFilteredCountries)
    }
  }
  
  private var originalList: MutableList<Country>? = null
  
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.fetchCountries()
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    adapter.submitList(originalList)
    imageBack.setOnClickListener {
      hideKeyboard(editTextSearch)
      popBackStack()
    }
    imageCross.setOnClickListener {
      editTextSearch.text.clear()
    }
    editTextSearch.setHint(R.string.hint_search_countries)
    editTextSearch.doAfterTextChanged { editable: Editable? ->
      if (editable.isNullOrBlank()) {
        adapter.submitList(originalList)
      } else {
        viewModel.filter(editable.toString())
      }
    }
  }
  
  override fun onResume() {
    super.onResume()
    editTextSearch.requestFocus()
    showKeyboard()
  }
  
  private fun handleAllList(countries: MutableList<Country>) {
    originalList = countries
    adapter.submitList(countries)
  }
  
  private fun handleFilteredCountries(countries: MutableList<Country>) {
    adapter.submitList(countries)
  }
}