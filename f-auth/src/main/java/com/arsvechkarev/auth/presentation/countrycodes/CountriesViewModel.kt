package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountries
import com.arsvechkarev.auth.presentation.countrycodes.CountriesAndCodesRepository.getCountriesAndCodes
import core.base.CoroutinesViewModel
import core.model.other.Country
import core.recycler.DisplayableItem
import javax.inject.Inject

class CountriesViewModel @Inject constructor() : CoroutinesViewModel() {
  
  val countriesAndCodes = MutableLiveData<List<DisplayableItem>>()
  private var fullList: List<DisplayableItem> = ArrayList()
  
  fun fetchCountriesAndCodes() {
    coroutine {
      if (fullList.isEmpty()) {
        fullList = getCountriesAndCodes()
      }
      countriesAndCodes.value = fullList
    }
  }
  
  fun filter(text: String) {
    coroutine {
      if (text.isBlank()) {
        countriesAndCodes.value = fullList
      } else {
        val filtered: List<Country>? = getCountries().filter {
          it.name.startsWith(text, ignoreCase = true)
        }
        countriesAndCodes.value = filtered
      }
    }
  }
  
}