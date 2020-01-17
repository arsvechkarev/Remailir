package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountries
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountriesAndCodes
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Default
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Searching
import core.base.CoroutinesViewModel
import core.model.other.Country
import core.recycler.DisplayableItem
import javax.inject.Inject

class CountriesViewModel @Inject constructor() : CoroutinesViewModel() {
  
  var currentState: State = Default
  
  val countriesAndCodes: LiveData<List<DisplayableItem>>
    get() = _countriesAndCodes
  
  private val _countriesAndCodes = MutableLiveData<List<DisplayableItem>>()
  
  fun fetchAll() {
    currentState = Default
    coroutine {
      _countriesAndCodes.value = getCountriesAndCodes()
    }
  }
  
  fun searchCountries(text: String) {
    currentState = Searching(text)
    coroutine {
      if (text.isBlank()) {
        _countriesAndCodes.value = getCountriesAndCodes()
      } else {
        val filtered: List<Country>? = getCountries().filter {
          it.name.startsWith(text, ignoreCase = true)
        }
        _countriesAndCodes.value = filtered
      }
    }
  }
  
  sealed class State {
    object Default : State()
    class Searching(val currentText: String = "") : State()
  }
}