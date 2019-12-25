package com.arsvechkarev.auth.presentation.countrycodes

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.list.CountryCodesAdapter
import com.arsvechkarev.auth.utils.CountryCodesHolder
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.hideKeyboard
import core.extensions.popBackStack
import core.extensions.showKeyboard
import core.model.other.Country
import core.viewdelegates.LayoutSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.layoutIncludedSearch
import kotlinx.android.synthetic.main.fragment_country_code_search.recyclerCountries

class CountryCodesSearchFragment : BaseFragment(), LayoutSearch {
  
  override val includedSearch: View by lazy { layoutIncludedSearch }
  
  override val layout: Int = R.layout.fragment_country_code_search
  
  private val originalList by lazy { CountryCodesHolder.countries }
  
  private val adapter = CountryCodesAdapter {
    entranceActivity.onCountrySelected(it)
    popBackStack()
    popBackStack()
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        val filteredList = ArrayList<Country>()
        originalList.filterTo(filteredList) {
          it.name.startsWith(editable.toString(), ignoreCase = true)
        }
        adapter.submitList(filteredList)
      }
    }
  }
  
  override fun onResume() {
    super.onResume()
    editTextSearch.requestFocus()
    showKeyboard()
  }
}