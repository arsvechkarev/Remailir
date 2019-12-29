package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesAdapter
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.hideKeyboard
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.showKeyboard
import core.model.other.Country
import kotlinx.android.synthetic.main.fragment_country_code_search.layoutIncludedSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.recyclerCountries

class SearchCountryFragment : BaseFragment() {
  
  override val includedSearch: View by lazy { layoutIncludedSearch }
  override val layout: Int = R.layout.fragment_country_code_search
  
  private val viewModel: SearchCountryViewModel by viewModels(
    factoryProducer = { SavedStateViewModelFactory(this) }
  )
  
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.fetchCountries()
    viewModel.countries.observe(this, ::handleList)
    editTextSearch.setText(viewModel.editTextString)
    recyclerCountries.adapter = adapter
    recyclerCountries.layoutManager = LinearLayoutManager(context)
    imageBack.setOnClickListener {
      hideKeyboard(editTextSearch)
      popBackStack()
    }
    imageCross.setOnClickListener {
      editTextSearch.text.clear()
    }
    editTextSearch.setHint(R.string.hint_search_countries)
    editTextSearch.doAfterTextChanged { editable: Editable? ->
      viewModel.filter(editable.toString())
    }
  }
  
  override fun onResume() {
    super.onResume()
    editTextSearch.requestFocus()
    showKeyboard()
  }
  
  private fun handleList(countries: MutableList<Country>) {
    adapter.submitList(countries)
  }
}