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
import core.model.other.Country
import core.util.observe
import core.util.popBackStack
import core.util.showKeyboard
import core.util.viewModelOf
import kotlinx.android.synthetic.main.fragment_country_code_search.recyclerCountries
import kotlinx.android.synthetic.main.fragment_country_code_search.searchToolbar
import javax.inject.Inject

class SearchCountryFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_country_code_search
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy {
    viewModelOf<SearchCountryViewModel>(viewModelFactory)
  }
  
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerAuthComponent.create().inject(this)
    viewModel.fetchCountries()
    viewModel.countries.observe(this, ::handleList)
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    searchToolbar.onBackClick {
      searchToolbar.hideKeyboard(context!!)
      popBackStack()
    }
    searchToolbar.onCrossClick {
      searchToolbar.editTextSearch.text.clear()
    }
    searchToolbar.editTextSearch.setHint(R.string.hint_search_countries)
    searchToolbar.editTextSearch.doAfterTextChanged { editable: Editable? ->
      viewModel.filter(editable.toString())
    }
  }
  
  override fun onResume() {
    super.onResume()
    searchToolbar.editTextSearch.requestFocus()
    showKeyboard()
  }
  
  private fun handleList(countries: MutableList<Country>) {
    adapter.submitList(countries)
  }
}