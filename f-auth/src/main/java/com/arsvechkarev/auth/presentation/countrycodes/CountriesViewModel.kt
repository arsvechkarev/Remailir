package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountries
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountriesAndCodes
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.SearchState.FullList
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.SearchState.SearchResultsList
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.ViewState.Default
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.ViewState.Searching
import core.base.CoroutinesViewModel
import core.model.other.Country
import core.recycler.DisplayableItem
import javax.inject.Inject

class CountriesViewModel @Inject constructor() : CoroutinesViewModel() {
  
  var currentState: ViewState = Default
  
  val countriesAndCodes: LiveData<SearchState>
    get() = _countriesAndCodes
  
  private val _countriesAndCodes = MutableLiveData<SearchState>()
  
  fun fetchAll() {
    currentState = Default
    coroutine {
      _countriesAndCodes.value = FullList(getCountriesAndCodes())
    }
  }
  
  fun searchCountries(text: String) {
    currentState = Searching(text)
    coroutine {
      if (text.isBlank()) {
        _countriesAndCodes.value = FullList(getCountriesAndCodes())
      } else {
        val filtered: List<Country> = getCountries().filter {
          it.name.startsWith(text, ignoreCase = true)
        }
        _countriesAndCodes.value = SearchResultsList(filtered)
      }
    }
  }
  
  sealed class SearchState(val list: List<DisplayableItem>) {
    class FullList(list: List<DisplayableItem>) : SearchState(list)
    class SearchResultsList(list: List<DisplayableItem>) : SearchState(list)
  }
  
  sealed class ViewState {
    object Default : ViewState()
    class Searching(val currentText: String = "") : ViewState()
  }
}