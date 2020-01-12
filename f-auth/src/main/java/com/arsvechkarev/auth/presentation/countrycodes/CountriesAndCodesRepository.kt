package com.arsvechkarev.auth.presentation.countrycodes

import com.arsvechkarev.auth.utils.getCountriesList
import com.arsvechkarev.auth.utils.getCountriesWithLetters
import core.CoroutinesDispatcherProvider
import core.model.other.Country
import core.recycler.DisplayableItem
import kotlinx.coroutines.withContext

object CountriesAndCodesRepository {
  
  private val dispatcherProvider = CoroutinesDispatcherProvider.DefaultImpl
  
  private var countries: MutableList<Country>? = null
  private var countriesAndCodes: MutableList<DisplayableItem>? = null
  
  suspend fun getCountries() = withContext(dispatcherProvider.Default) {
    if (countries == null) {
      countries = getCountriesList()
    }
    return@withContext countries!!
  }
  
  suspend fun getCountriesAndCodes() = withContext(dispatcherProvider.Default) {
    if (countriesAndCodes == null) {
      countriesAndCodes = getCountriesWithLetters(getCountries())
    }
    return@withContext countriesAndCodes!!
  }
  
}