package com.arsvechkarev.auth.utils

import androidx.core.util.forEach
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