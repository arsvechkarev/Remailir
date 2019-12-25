package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import core.base.BaseViewModel
import core.model.other.Country
import javax.inject.Inject

class SearchCountryViewModel @Inject constructor() : BaseViewModel() {
  
  val allCountries = MutableLiveData<MutableList<Country>>()
  val filteredCountries = MutableLiveData<MutableList<Country>>()
  
  fun fetchCountries() {
    coroutine {
      allCountries.value = CountriesAndCodesRepository.getCountries()
    }
  }
  
  fun filter(text: String) {
    coroutine {
      val filtered: List<Country>? = allCountries.value?.filter {
        it.name.startsWith(text, ignoreCase = true)
      }
      filteredCountries.value = filtered as MutableList<Country>?
    }
  }
  
}