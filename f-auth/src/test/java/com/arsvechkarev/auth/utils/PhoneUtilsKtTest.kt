package com.arsvechkarev.auth.utils

import androidx.core.util.forEach
import com.arsvechkarev.core.model.Country
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.junit.Test
import java.util.Locale

class PhoneUtilsKtTest {
  
  @Test
  fun test() {
    val map = createCountryCodeToRegionCodeMap()
    map.forEach { key, value ->
      println("$key -> $value")
    }
  }
  
  @Test
  fun displC2() {
    val countries = Locale.getISOCountries()
    countries.forEach {
      println(it)
      val locale = Locale("", it)
      val countryName = locale.displayCountry
      println(countryName)
    }
    println(countries.size)
  }
  
  
  @Test
  fun getCountriesAndCodes() {
    val countries = ArrayList<Country>()
    val phoneNumberUtils = PhoneNumberUtil.getInstance()
    Locale.getISOCountries().forEach {
      val locale = Locale("", it)
      val codeForRegion = phoneNumberUtils.getCountryCodeForRegion(it)
      if (codeForRegion != 0) {
        val country =
          Country(locale.displayName, it, codeForRegion.toString())
        println(country)
        countries.add(country)
      }
    }
  }
  
  @Test
  fun displayingCountries1() {
    val locales = Locale.getAvailableLocales()
    val countries = ArrayList<String>()
    for (locale in locales) {
      val country = locale.displayCountry
      if (country.trim().isNotEmpty()) {
        countries.add(country)
      }
    }
    countries.sort()
    for (country in countries) {
      println(country)
    }
    println("# countries found: " + countries.size)
  }
}