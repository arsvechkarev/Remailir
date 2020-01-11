package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import core.base.CoroutinesViewModel
import core.recycler.DisplayableItem
import javax.inject.Inject

class CountriesViewModel @Inject constructor() : CoroutinesViewModel() {
  
  val countriesAndCodes = MutableLiveData<MutableList<DisplayableItem>>()
  
  fun fetchCountriesAndCodes() {
    coroutine {
      countriesAndCodes.value = CountriesAndCodesRepository.getCountriesAndCodes()
    }
  }
  
}