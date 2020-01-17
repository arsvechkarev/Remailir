package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountries
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountriesAndCodes
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Default
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel.State.Searching
import core.base.CoroutinesViewModel
import core.model.other.Country
import core.recycler.DisplayableItem
import log.log
import javax.inject.Inject

class CountriesViewModel @Inject constructor() : CoroutinesViewModel() {
  
  val countriesAndCodes = MutableLiveData<List<DisplayableItem>>()
  
  var currentState: State = Default
  
  fun fetchAll() {
    currentState = Default
    coroutine {
      countriesAndCodes.value = getCountriesAndCodes()
    }
  }
  
  fun searchCountries(text: String) {
    currentState = Searching(text)
    log { "== vm searching, text = '$text' ==" }
    coroutine {
      if (text.isBlank()) {
        countriesAndCodes.value = getCountriesAndCodes()
      } else {
        val filtered: List<Country>? = getCountries().filter {
          it.name.startsWith(text, ignoreCase = true)
        }
        countriesAndCodes.value = filtered
      }
    }
  }
  
  sealed class State {
    object Default : State()
    class Searching(val currentText: String = "") : State()
  }
}