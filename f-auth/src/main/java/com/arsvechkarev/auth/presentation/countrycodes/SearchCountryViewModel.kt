package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import core.base.CoroutinesViewModel
import core.model.other.Country
import javax.inject.Inject

class SearchCountryViewModel @Inject constructor() : CoroutinesViewModel() {
  
  val countries = MutableLiveData<MutableList<Country>>()
  
  fun fetchCountries() {
    coroutine {
      countries.value = CountriesAndCodesRepository.getCountries()
    }
  }
  
  fun filter(text: String) {
    coroutine {
      if (text.isBlank()) {
        countries.value = CountriesAndCodesRepository.getCountries()
      } else {
        val filtered: List<Country>? = countries.value?.filter {
          it.name.startsWith(text, ignoreCase = true)
        }
        countries.value = filtered as MutableList<Country>?
      }
    }
  }
  
}