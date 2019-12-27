package com.arsvechkarev.auth.presentation.countrycodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import core.base.BaseViewModel
import core.model.other.Country

class SearchCountryViewModel(
  private val savingStateHandle: SavedStateHandle
) : BaseViewModel() {
  
  companion object {
    const val EDIT_TEXT_VALUE = "EDIT_TEXT_VALUE"
  }
  
  val editTextString = savingStateHandle[EDIT_TEXT_VALUE] ?: ""
  
  
  private val countries = MutableLiveData<MutableList<Country>>()
  
  fun fetchCountries() {
    coroutine {
      countries.value = CountriesAndCodesRepository.getCountries()
    }
  }
  
  fun filter(text: String) {
    savingStateHandle.set(EDIT_TEXT_VALUE, text)
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